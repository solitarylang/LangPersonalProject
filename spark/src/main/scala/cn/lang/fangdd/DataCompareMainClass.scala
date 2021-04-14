package cn.lang.fangdd

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.slf4j.{Logger, LoggerFactory}

/**
 * 严格对比的main方法
 *
 * @author lang
 * @since 4/7/21 1:52 PM
 */
object DataCompareMainClass {
  private val logger: Logger = LoggerFactory.getLogger(DataCompareMainClass.getClass)

  private var spark: SparkSession = _

  private var dwDataFrame: DataFrame = _
  private var mrsDataFrame: DataFrame = _
  private var columns: Seq[String] = _

  /**
   * main
   *
   * @param args
   */
  def main(args: Array[String]): Unit = {

    init()

    SparkRuleConstants.NEED_COMPARE_TABLES.foreach(tableName => {
      read(tableName)
      compare(tableName)
      clear()
    })
  }

  /**
   * init
   */
  def init(): Unit = {
    logger.info("start init SparkSession ...")

    spark = SparkSession.builder()
      .appName("compare_data")
      .config("hive.metastore.uris", SparkRuleConstants.MRS_WAREHOUSE_HIVE_META_STORE_URI)
      .enableHiveSupport()
      .master("local[*]")
      .getOrCreate()
  }

  /**
   * read data from both source into DataFrame
   *
   * @param tableName
   */
  def read(tableName: String): Unit = {
    logger.info("-------------------------------------------")
    logger.info(s"start read both data with (${tableName}) ...")
    logger.info("-------------------------------------------")

    HadoopConf.changeHadoopConf(spark,
      SparkRuleConstants.MRS_WAREHOUSE_HADOOP_NAMESPACE,
      SparkRuleConstants.MRS_WAREHOUSE_HADOOP_NN1,
      SparkRuleConstants.MRS_WAREHOUSE_HADOOP_NN1_ADDR,
      SparkRuleConstants.MRS_WAREHOUSE_HADOOP_NN2,
      SparkRuleConstants.MRS_WAREHOUSE_HADOOP_NN2_ADDR)

    logger.info(s"start read data from path in parquet ...")
    dwDataFrame = spark.read.parquet(SparkRuleConstants.PATH_RULE.replaceAll("tableName", tableName))
    val srcCount = dwDataFrame.count()
    logger.info(s"data warehouse count in ${tableName}: ${srcCount}")

    logger.info("start read data from mrs hive ...")
    mrsDataFrame = spark.sql(s"SELECT * FROM ${tableName} WHERE dt='${SparkRuleConstants.LATEST_PARTITION}'")
    val destCount = mrsDataFrame.count()
    logger.info(s"mrs warehouse count in ${tableName}: ${destCount}")

    columns = mrsDataFrame.schema.map(_.name)
    logger.info(s"mrs DataFrame schema columns: ${columns}")

    if (srcCount != destCount) {
      logger.error(s"${tableName}双跑情况下数据量级不一致,srcCount=${srcCount}, destCount=${destCount}")
    }
  }

  /**
   * primary key
   *
   * @param tableName
   * @return
   */
  def getPrimaryKey(tableName: String): List[String] = {
    List.empty
  }

  /**
   * strict compare
   *
   * @param tableName 表名
   */
  def compare(tableName: String): Unit = {
    logger.info("-------------------------------------------")

    val result = dwDataFrame.join(mrsDataFrame, columns)
    val bothCount = result.count()

    // TODO 需要将不一致的数据打印到日志中-可以设置batchSizeThreshold，超过不打印
    logger.info(s"both exist count in ${tableName}: ${bothCount}")
    logger.info("-------------------------------------------")
  }

  /**
   * clear
   */
  def clear(): Unit = {
    logger.warn("start clear the DataFrame and columns ...")
    dwDataFrame = null
    mrsDataFrame = null
    columns = null
  }
}
