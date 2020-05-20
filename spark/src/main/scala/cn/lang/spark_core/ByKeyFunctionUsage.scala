package cn.lang.spark_core

import org.apache.spark.sql.SparkSession

/**
 * 找出互相关注的qq对
 */
object ByKeyFunctionUsage {
  def main(args: Array[String]): Unit = {
    // env
    val spark: SparkSession = SparkSession
      .builder()
      .appName("ContinuousLoginDays")
      .master("local[*]")
      .getOrCreate()
    val sc = spark.sparkContext

    // source
    val raw_data = sc.textFile("/Users/langjiang/Ideaproject/Github/LangPersonalProject/spark/src/main/resources/cn/lang/spark_core/file.txt")

    // transform
    val result = raw_data
      .filter(iterm => {
        val pairs = iterm.split(",")
        if (pairs.length == 2) true
        else false
      })
      .map(iterm => {
        val pairs = iterm.split(",")
        val qqa = pairs(0).toInt
        val qqb = pairs(1).toInt
        if (qqa <= qqb) {
          (qqa + ":" + qqb, 1)
        } else {
          (qqb + ":" + qqa, 1)
        }
      })
      .countByKey()
      .filter(_._2 == 2)

    // sink
    result.foreach(println(_))
  }
}
