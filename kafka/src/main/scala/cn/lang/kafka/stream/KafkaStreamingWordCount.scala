package cn.lang.kafka.stream

import java.lang.Thread.UncaughtExceptionHandler
import java.util.Properties

import org.apache.kafka.common.serialization.Serdes.StringSerde
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.apache.kafka.streams.kstream.{KStream, ValueMapper}
import org.apache.kafka.streams.scala.Serdes
import org.apache.kafka.streams.scala.kstream.{Consumed, Produced}
import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig}

/**
 * @author jianglang 2020年05月30日17:55:18
 */
object KafkaStreamingWordCount {
  def main(args: Array[String]): Unit = {
    // 一、创建一个KafkaStreams的实例，包含两个参数
    // 1.处理拓扑，
    //    StreamsBuilder#build() for the DSL
    //    Topology for the Processor API
    val builder: StreamsBuilder = new StreamsBuilder
    val topology = builder.build()
    // 2.参数，java.util.Properties
    val streamsConfig = new Properties()
    // 2.1参数赋值，格式类似于其他流处理添加kafka的相关参数
    // 2.2其他参数说明可见[streamsconfigs](http://kafka.apache.org/25/documentation/#streamsconfigs)
    streamsConfig.put(StreamsConfig.APPLICATION_ID_CONFIG, "KafkaStreamingWordCount")
    streamsConfig.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
//    streamsConfig.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, StringSerializer)
//    streamsConfig.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, StringSerializer)
    // 根据上述参数创建实例
    val streams: KafkaStreams = new KafkaStreams(topology, streamsConfig)

    // 二、创建流
    val input_topic = "first"
    val output_topic = "second"
    val source: KStream[String, String] = builder.stream(input_topic, Consumed.`with`(Serdes.String, Serdes.String))

    source.to(output_topic,Produced.`with`(Serdes.String, Serdes.String))

    // 在启动之前开启线程调用handler处理未知异常
    streams.setUncaughtExceptionHandler(new UncaughtExceptionHandler {
      override def uncaughtException(t: Thread, e: Throwable): Unit = {
        // 添加对异常的处理方式
      }
    })
    // 为了应用优雅的关闭，可以开启钩子函数去关闭流应用
    Runtime.getRuntime.addShutdownHook(new Thread("streams-shutdown-hook") {
      override def run(): Unit = {
        streams.close()
      }
    })

    // 框架已经初始化，但是还没开始处理，需要显示的启用KafkaStreams#start()
    try {
      streams.start()
    } catch {
      case _: Throwable => System.exit(1)
    }
  }
}
