package cn.lang.flink.custom

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.configuration.Configuration
import org.apache.flink.metrics.Counter
import org.apache.flink.streaming.api.scala._

/**
 * @author ：jianglang
 * @date ：Created in 2020/5/3 7:42 AM
 * @description ：自定义自己的map方法，实现诸如获取计数器等更多的功能，
 * @version ：1.0.0
 */
class MyMapFunction extends RichMapFunction[String, String] {
  private var counter: Counter = _;

  override def open(parameters: Configuration): Unit = {
    this.counter = this.getRuntimeContext.getMetricGroup.counter("MyCounter")
  }

  override def map(in: String): String = {
    if (in == null) {
      null
    } else if (in.length <= 10) {
      in
    } else {
      this.counter.inc(1L)
      in.substring(0, 10)
    }
  }

  override def close(): Unit = {
    val context = this.getRuntimeContext
    println("context.getTaskName: " + context.getTaskName)

    println("this.counter.getCount" + this.counter.getCount)
  }
}

object MyMapFunction {
  def main(args: Array[String]): Unit = {
    val host = "localhost"
    val port = 9999
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    /** source */
    val raw_stream: DataStream[String] = env.socketTextStream(host, port)

    /** transformation */
    val result = raw_stream.map(new MyMapFunction)

    /** sink */
    result.print()

    env.execute("MyMapFunction")
  }
}