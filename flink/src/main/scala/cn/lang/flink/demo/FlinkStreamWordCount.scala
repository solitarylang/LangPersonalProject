package cn.lang.flink.demo

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

object FlinkStreamWordCount {
  def main(args: Array[String]): Unit = {
    // 定义监听的地址和端口
    val host = "localhost"
    val port = 9999
    // env
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    /** source */
    // 监听端口，可在命令行执行 {nc -lk 9999} 输入数据
    val raw_stream: DataStream[String] = env.socketTextStream(host, port)

    // avoid could not find the implicit
    import org.apache.flink.api.scala._
    /** transformation */
    val result: DataStream[(String, Int)] = raw_stream
      .flatMap(_.split(" ")) // 对每行数据按照空格进行分割
      .map((_, 1)) // 对每一个单词进行计数
      .keyBy(0) // 按照索引位在0号的单词进行分组，这里注意和DataSet API的区别
      .sum(1) // 按照索引位在1号的单词数量进行加和

    /** sink */
    result.print()
    // 连续输入4个flink单词之后的打印情况
    //    13> (flink,1)
    //    13> (flink,2)
    //    13> (flink,3)
    //    13> (flink,4) 这里的结果会一直累加，注意和后面窗口函数的区别

    // execute the stream work
    env.execute("FlinkStreamWordCount")
  }
}
