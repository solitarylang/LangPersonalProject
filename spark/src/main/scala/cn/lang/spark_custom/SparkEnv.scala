package cn.lang.spark_custom

import org.apache.spark.sql.SparkSession
import org.slf4j.{Logger, LoggerFactory}

object SparkEnv {
  /** initialization */
  private val logger: Logger = LoggerFactory.getLogger(SparkEnv.getClass)
  private var spark: SparkSession = _

  /** SparkSession */
  def getSparkSession(app_name: String = "sparkpro"): SparkSession = {
    /** win or mac for local test */
    if (System.getProperty("os.name").toLowerCase.startsWith("mac") ||
      System.getProperty("os.name").toLowerCase.startsWith("win")) {
      spark = SparkSession.builder().appName(app_name)
        .config("spark.sql.caseSensitive", "true")
        .enableHiveSupport()
        .master("local[*]").getOrCreate()
    } else {
      spark = SparkSession.builder().appName(app_name)
        .config("parquet.enable.dictionary", "false")
        .config("hive.exec.dynamic.partition", "true")
        .config("hive.exec.dynamic.partition.mode", "nonstrict")
        .config("spark.sql.caseSensitive", "true")
        .enableHiveSupport()
        .getOrCreate()
    }
    logger.info("====================== SPARK ENV INISTIALIZATION ======================")
    spark
  }

}
