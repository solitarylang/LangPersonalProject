package cn.lang.flink.demo

import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.TableEnvironment

case class Event(uid: String, timestamp: Long)

object FlinkTableApiWordCount {
  def main(args: Array[String]): Unit = {
    // env
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val ip = "localhost"
    val port = 9999
    val rawStream: DataStream[Event] = env.socketTextStream(ip, port)
      .map(line => Event(line.split(" ")(0), line.split(" ")(1).toLong))
    // 从现有流进行转换
    val tableEnv = TableEnvironment.getTableEnvironment(env)
    val tableStream = tableEnv.fromDataStream(rawStream)

    val result = tableStream
      .groupBy("uid")

  }
}
