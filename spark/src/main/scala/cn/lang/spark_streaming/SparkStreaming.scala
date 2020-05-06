package cn.lang.spark_streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @author ：jianglang
 * @date ：Created in 2020/4/29 7:44 PM
 * @description ：熟悉spark streaming相关的操作
 * @version ：1.0.0
 */
object SparkStreaming {
  def main(args: Array[String]): Unit = {
    // create a env
    val conf = new SparkConf().setMaster("local[*]").setAppName("NetworkWordCount")
    val ssc = new StreamingContext(conf, Seconds(10))

    // Create a DStream that will connect to hostname:port, like localhost:9999
    val raw_data = ssc.socketTextStream("localhost", 9999)

    val result = raw_data.map((_, 1)).reduceByKey(_ + _)

    result.print()

    ssc.start()
    ssc.awaitTermination() // Wait for the computation to terminate
  }
}
