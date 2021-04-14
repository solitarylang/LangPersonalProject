package cn.lang.fangdd

import org.apache.spark.sql.{SaveMode, SparkSession}
import org.slf4j.{Logger, LoggerFactory}

/**
 * @author lang
 * @since 4/7/21 11:20 AM
 */
object DownloadDataFromHive {
  private val logger: Logger = LoggerFactory.getLogger(DownloadDataFromHive.getClass)

  /**
   * main
   *
   * @param args
   */
  def main(args: Array[String]): Unit = {
    logger.info("DownloadDataFromHive main class starting ...")

    SparkRuleConstants.NEED_COMPARE_TABLES.foreach(tableName => {
      download(tableName)
    })
  }

  /**
   * @param tableName 表名
   * @param isSource  集群表示，true代表是数仓集群，false代表是MRS集群
   */
  def download(tableName: String, isSource: Boolean): Unit = {
    logger.info(s"start download tableName:${tableName} ...")

    var hiveMetaStoreAddress = ""

    isSource match {
      // data warehouse
      case true => {
        hiveMetaStoreAddress = SparkRuleConstants.DATA_WAREHOUSE_HIVE_META_STORE_URI
      }
      // data mrs
      case false => {
        hiveMetaStoreAddress = SparkRuleConstants.MRS_WAREHOUSE_HIVE_META_STORE_URI
      }
      case _ => throw new Exception("argument wrong")
    }

    logger.info("hiveMetaStoreAddress: " + hiveMetaStoreAddress)

    val spark = SparkSession.builder()
      .appName(s"download_${tableName}")
      .config("hive.metastore.uris", hiveMetaStoreAddress)
      .enableHiveSupport()
      .master("local[*]")
      .getOrCreate()

    // 设置source hadoop的有关信息
    //    HadoopConf.changeHadoopConf(spark,
    //      SparkRuleConstants.DATA_WAREHOUSE_HADOOP_NAMESPACE,
    //      SparkRuleConstants.DATA_WAREHOUSE_HADOOP_NN1,
    //      SparkRuleConstants.DATA_WAREHOUSE_HADOOP_NN1_ADDR,
    //      SparkRuleConstants.DATA_WAREHOUSE_HADOOP_NN2,
    //      SparkRuleConstants.DATA_WAREHOUSE_HADOOP_NN2_ADDR)

    HadoopConf.changeHadoopConf(spark,
      "dw/core-site.xml",
      "dw/hdfs-site.xml",
      "dw/hive-site.xml")

    val queryLatestPartitionSql = s"SELECT * FROM ${tableName} WHERE dt='${SparkRuleConstants.LATEST_PARTITION}'"

    logger.info(s"queryLatestPartitionSql: ${queryLatestPartitionSql}")

    val result = spark.sql(queryLatestPartitionSql)

    logger.info("Schema: " + result.schema.map(_.name).toString())
    logger.info(s"${tableName}的${SparkRuleConstants.LATEST_PARTITION}分区查询结果的条数${result.count()}")
    val outputPath = SparkRuleConstants.PATH_RULE.replaceAll("tableName", tableName)
    logger.info(s"准备往${outputPath} 追加数据 ...")

    // 要将数据写出的时候调整hadoop的配置信息
    HadoopConf.changeHadoopConf(spark,
      SparkRuleConstants.MRS_WAREHOUSE_HADOOP_NAMESPACE,
      SparkRuleConstants.MRS_WAREHOUSE_HADOOP_NN1,
      SparkRuleConstants.MRS_WAREHOUSE_HADOOP_NN1_ADDR,
      SparkRuleConstants.MRS_WAREHOUSE_HADOOP_NN2,
      SparkRuleConstants.MRS_WAREHOUSE_HADOOP_NN2_ADDR)

    result.write
      .mode(SaveMode.Append)
      .parquet(outputPath)

    logger.info("开始关闭spark对象 ...")
    spark.close()
  }

  /**
   * @param tableName 表名
   */
  def download(tableName: String): Unit = {
    download(tableName, true)
  }
}
