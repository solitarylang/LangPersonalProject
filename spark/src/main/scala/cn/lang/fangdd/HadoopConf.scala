package cn.lang.fangdd

import org.apache.hadoop.fs.Path
import org.apache.spark.sql.SparkSession
import org.slf4j.{Logger, LoggerFactory}

/**
 * @author lang
 * @since 4/7/21 4:06 PM
 */
object HadoopConf {
  private val logger: Logger = LoggerFactory.getLogger(HadoopConf.getClass)

  /**
   * @param spark     SparkSession
   * @param nameSpace Namespace
   * @param nn1       Namenode 1
   * @param nn1Addr   Namenode 1 ip:port
   * @param nn2       Namenode 2
   * @param nn2Addr   Namenode 2 ip:port
   */
  def changeHadoopConf(spark: SparkSession,
                       nameSpace: String,
                       nn1: String, nn1Addr: String,
                       nn2: String, nn2Addr: String): Unit = {
    logger.info(s"开始设置hadoop有关的信息，nameSpace=${nameSpace}, nn1=${nn1}, " +
      s"nn1Addr=${nn1Addr}, nn2=${nn2}, nn2Addr=${nn2Addr}")

    val sc = spark.sparkContext

    sc.hadoopConfiguration.set("fs.defaultFS", s"hdfs://${nameSpace}")
    sc.hadoopConfiguration.set("dfs.nameservices", nameSpace)
    sc.hadoopConfiguration.set(s"dfs.ha.namenodes.${nameSpace}", s"${nn1},${nn2}")
    sc.hadoopConfiguration.set(s"dfs.namenode.rpc-address.${nameSpace}.${nn1}", nn1Addr)
    sc.hadoopConfiguration.set(s"dfs.namenode.rpc-address.${nameSpace}.${nn2}", nn2Addr)
  }

  /**
   * change Configuration
   *
   * @param spark
   * @param corePath
   * @param hdfsPath
   * @param hivePath
   */
  def changeHadoopConf(spark: SparkSession,
                       corePath: String,
                       hdfsPath: String,
                       hivePath: String): Unit = {
    logger.info(s"开始设置hadoop有关的信息，corePath=${corePath}, hdfsPath=${hdfsPath}, " +
      s"hivePath=${hivePath}")

    val sc = spark.sparkContext

    sc.hadoopConfiguration.addResource(new Path(corePath))
    sc.hadoopConfiguration.addResource(new Path(hdfsPath))
    sc.hadoopConfiguration.addResource(new Path(hivePath))
  }
}
