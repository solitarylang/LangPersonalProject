package cn.lang.flink.demo

import java.util.Properties

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011
import org.apache.flink.streaming.util.serialization.SimpleStringSchema

/**
 * watermark
 * watermark = maxEventTime - delayTimeInternal(different within every record)
 * invoke window calculate while watermark > timeWindowDeadline
 **/
object KafkaFlinkStreamEventTime {
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
    // source
    val transaction = env.addSource(
      new FlinkKafkaConsumer011[String](IN_KAFKA_TOPIC, new SimpleStringSchema(), kafkaProps))
      .setParallelism(4)

    /** transform */
    val result: DataStream[(Long, Int)] = transaction
      .map(_.toLong) // 传入的每一条数据都是10位时间戳
      .assignTimestampsAndWatermarks(
        new BoundedOutOfOrdernessTimestampExtractor[Long](Time.milliseconds(1000)) {
          override def extractTimestamp(element: Long): Long = element * 1000
        }
      )
      .map(iterm => (iterm, 1))
      .keyBy(0)
      .timeWindow(Time.seconds(10), Time.seconds(5)) // 10s滑动串口，5s滑动步长
      .reduce((history, add) => (history._1, history._2 + add._2))

    // sink
    result.map(iterm => iterm.toString() + " : " + System.currentTimeMillis()).print()

    env.execute("KafkaFlinkStreamEventTime")
  }
}

// 这是业务中数据按照对应格式封装的样例类，其中包含有event time
case class LogsCase(uid: Long, timestamp: Long) // 这里上报时间戳默认是10位

// 自定义获取水位的类，也可以使用BoundedOutOfOrdernessTimestampExtractor匿名实现类
class MyBoundedOutOfOrdernessTimestampExtractor extends AssignerWithPeriodicWatermarks[LogsCase] {
  // 获取当前的水位
  override def getCurrentWatermark: Watermark = ???

  // 从数据样例类中抽取时间戳
  override def extractTimestamp(element: LogsCase, previousElementTimestamp: Long): Long = {
    null
  }
}