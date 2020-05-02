package cn.lang.flink.demo

import java.util.Properties
import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer011, FlinkKafkaProducer011}
import org.apache.flink.streaming.util.serialization.SimpleStringSchema

object KafkaFlinkStreamTimeWindow {
  def main(args: Array[String]): Unit = {
    // environment
    val env = StreamExecutionEnvironment.getExecutionEnvironment
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
    val transaction: DataStream[String] = env.addSource(
      new FlinkKafkaConsumer011[String](IN_KAFKA_TOPIC, new SimpleStringSchema(), kafkaProps))

    /** transform1 */
    // 每10s内上报正常和异常日志的条数
    //    val result = transaction
    //      .map(iterm => (iterm.contains("error"), 1)) // 如果包含某一条带有error的日志，那么将添加统计
    //      .keyBy(0) // 按照索引位为0的是否正常日志进行分组，分别统计正常与否的条数
    //      .timeWindow(Time.seconds(10)) // 设定窗口10s，这里只有一个参数，启动的是滚动窗口
    //      // 启动的是return window(TumblingProcessingTimeWindows.of(size))
    //      .reduce((history, add) => (history._1, history._2 + add._2)) // 可以理解为累加器的update

    // 每过10s打印一次
    //    1> (false,2) : 1588391750005
    //    10> (true,1) : 1588391760003
    //    10> (true,4) : 1588391770002
    //    10> (true,1) : 1588391780000

    /** transform2 */
    // 最近15s内上报日志异常条数
    val result: DataStream[(String, Int)] = transaction
      .map(iterm => (iterm, 1)) // 如果包含某一条带有error的日志，那么将添加统计
      .filter(_._1.contains("error")) // 过滤正常的日志
      .keyBy(0) // 按照索引位为0号的日志本身以及是否正常进行分组
      .timeWindow(Time.seconds(15), Time.seconds(5)) // 窗口设置为15s,滑动步长设置为5s
      .reduce((history, add) => (history._1, history._2 + add._2)) // 核心是理解reduce方法需要传入的是什么

    // 每过5s打印一次
    //    14> (error,1) : 1588392335002
    //    14> (error,3) : 1588392340001
    //    14> (error,3) : 1588392345004
    //    14> (error,2) : 1588392350003

    // sink
    result.map(iterm => iterm.toString() + " : " + System.currentTimeMillis()).print()
    env.execute("KafkaFlinkStreamTimeWindow")
  }
}
