package cn.lang.flink.demo

import java.text.SimpleDateFormat
import java.util.Properties

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011
import org.apache.flink.streaming.util.serialization.SimpleStringSchema

import scala.collection.mutable

/**
 * watermark
 * watermark = maxEventTime - delayTimeInternal(different within every record)
 * invoke window calculate while watermark > timeWindowDeadline
 **/
object KafkaFlinkStreamEventTime {

  // 返回的是13位时间戳，精确度到毫秒
  def dataToTimestamp(date: String): Long = {
    val sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss")
    sdf.parse(date).getTime
  }

  case class Event(uid: String, timestamp: Long) // 这里上报时间戳默认是10位

  def main(args: Array[String]): Unit = {
    // environment
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    // attention site imported from别导错包，这里是启用event time的机制
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    // property
    val ZOOKEEPER_HOST = "localhost:2181"
    val KAFKA_BROKER = "localhost:9092"
    val TRANSACTION_GROUP = "transaction"
    val IN_KAFKA_TOPIC = "first"
    // set
    val kafkaProps = new Properties()
    kafkaProps.setProperty("zookeeper.connect", ZOOKEEPER_HOST)
    kafkaProps.setProperty("bootstrap.servers", KAFKA_BROKER)
    kafkaProps.setProperty("group.id", TRANSACTION_GROUP)
    kafkaProps.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaProps.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaProps.setProperty("auto.offset.reset", "latest")

    // source，这里输入的每一条数据格式：
    //    1,2020年05月02日17:26:16
    //    1,2020年05月02日17:26:17
    //    1,2020年05月02日17:26:18
    //    1,2020年05月02日17:26:19
    //    1,2020年05月02日17:26:20
    //    1,2020年05月02日17:26:21
    //    1,2020年05月02日17:26:22
    //    1,2020年05月02日17:26:28
    val transaction = env.addSource(
      new FlinkKafkaConsumer011[String](IN_KAFKA_TOPIC, new SimpleStringSchema(), kafkaProps))
      .setParallelism(1)

    /** transform */
    /* 转换需求是将同一个uid窗口内上报的所有数据 */
    val result: DataStream[Event] = transaction
      .map(iterm => Event(iterm.split(",")(0), dataToTimestamp(iterm.split(",")(1)))) // 传入的每一条数据都是13位时间戳
      .assignTimestampsAndWatermarks(
        new BoundedOutOfOrdernessTimestampExtractor[Event](Time.milliseconds(1000)) {
          override def extractTimestamp(element: Event): Long = element.timestamp
        }
      )
    // 将转换的数据打印出来
    //    result.map(i => println("Infos In Event is: " + i.uid + "-" + i.timestamp))

    val result1: DataStream[(String, Long)] = result
      .map(event => (event.uid, event.timestamp))
      .keyBy(0)
      .window(TumblingEventTimeWindows.of(Time.seconds(5)))
      .minBy(1)

    // sink
    result1.map(iterm => println("Infos In Result is: " + iterm._1 + "-" + iterm._2 + "==" + System.currentTimeMillis()))
    //    Infos In Result is: 1-1588411576000==1589552826008
    //    Infos In Result is: 1-1588411580000==1589552826011
    //    Infos In Result is: 1-1588411588000==1589552826011
    env.execute("KafkaFlinkStreamEventTime")
  }
}

// 水印机制的类，也可以使用BoundedOutOfOrdernessTimestampExtractor匿名实现类
class MyBoundedOutOfOrdernessTimestampExtractor(delayInterval: Long) extends AssignerWithPeriodicWatermarks[Event] {
  // 上一个发送的水印值(也就是上一个触发窗口时的水印值)
  var lastEmittedWatermark: Long = 0L
  // 当前进入所有数据中最大的event time和上一次发送水印值的差值
  var maxOutOfOrderness: Long = delayInterval
  // 当前进入所有数据中最大的event time
  var currentMaxTimestamp: Long = lastEmittedWatermark + this.maxOutOfOrderness

  // 获取当前的水印
  override def getCurrentWatermark: Watermark = {
    val tmp = this.currentMaxTimestamp - this.maxOutOfOrderness
    if (tmp >= lastEmittedWatermark) {
      lastEmittedWatermark = tmp
    }
    new Watermark(lastEmittedWatermark)
  }

  // 从数据样例类中抽取时间戳
  override def extractTimestamp(element: Event, previousElementTimestamp: Long): Long = {
    val tmp = element.timestamp
    if (tmp > currentMaxTimestamp) {
      currentMaxTimestamp = tmp
    }
    println(element.toString + "--" + tmp + "-" + currentMaxTimestamp)
    tmp
  }
}