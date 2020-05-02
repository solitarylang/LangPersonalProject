package cn.lang.flink.demo

import java.util.Properties

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011
import org.apache.flink.streaming.util.serialization.SimpleStringSchema

object KafkaFlinkStreamWordCount {
  def main(args: Array[String]): Unit = {
    // environment
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    // property 生产中可以读args也可以读配置文件
    val ZOOKEEPER_HOST = "localhost:2181"
    val KAFKA_BROKER = "localhost:9092"
    val TRANSACTION_GROUP = "transaction"
    val IN_KAFKA_TOPIC = "first"

    // set kafka properties
    val kafkaProps = new Properties()
    kafkaProps.setProperty("zookeeper.connect", ZOOKEEPER_HOST)
    kafkaProps.setProperty("bootstrap.servers", KAFKA_BROKER)
    kafkaProps.setProperty("group.id", TRANSACTION_GROUP)
    kafkaProps.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaProps.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaProps.setProperty("auto.offset.reset", "latest")

    // source
    // 本地执行 启动zk和kafka，以及console的生产者
    // /Users/langjiang/Software/zookeeper-3.4.10/bin/zkServer.sh start
    // /Users/langjiang/Software/kafka_2.11-2.1.0/bin/kafka-server-start.sh -daemon /Users/langjiang/Software/kafka_2.11-2.1.0/config/server.properties
    // /Users/langjiang/Software/kafka_2.11-2.1.0/bin/kafka-server-stop.sh
    val transaction: DataStream[String] = env.addSource(
      new FlinkKafkaConsumer011[String](IN_KAFKA_TOPIC, new SimpleStringSchema(), kafkaProps))

    // transform
    val result= transaction
      .flatMap(_.split(" ")) // 对每行数据按照空格进行分割
      .map((_, 1)) // 对每一个单词进行计数
      .keyBy(0) // 按照索引位在0号的单词进行分组，这里注意和DataSet API的区别
      .sum(1) // 按照索引位在1号的单词数量进行加和

    // sink1
    // 将最终的结果写回到Kafka
    //    val OUT_KAFKA_TOPIC = "second"
    //    result.addSink(new FlinkKafkaProducer011[(String)](KAFKA_BROKER, OUT_KAFKA_TOPIC, new SimpleStringSchema()))

    // sink2
    result.print()
    //    13> (flink,1) // 前面的数字代表的是计算的core
    //    13> (flink,2)
    //    13> (flink,3)
    //    13> (flink,4)
    //    13> (flink,5)
    //    1> (spark,1)
    env.execute("KafkaFlinkStreamWordCount")
  }
}
