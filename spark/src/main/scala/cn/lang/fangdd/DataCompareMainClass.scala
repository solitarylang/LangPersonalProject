package cn.lang.fangdd

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
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

    SparkRuleConstants.NEED_COMPARE_TABLES_IN_PARQUET.foreach(tableName => {
      read(tableName)
      compare(tableName)
      clear()
    })
  }

  /**
   * init
   */
  def init(): Unit = {
    logger.warn("start init SparkSession ...")

    spark = SparkSession.builder()
      .appName("compare_data")
      .config("hive.metastore.uris", SparkRuleConstants.MRS_WAREHOUSE_HIVE_META_STORE_URI)
      .config("spark.yarn.am.waitTime", 1000)
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
    logger.warn("-------------------------------------------")
    logger.warn(s"start read both data with (${tableName}) ...")
    logger.warn("-------------------------------------------")

    HadoopConf.changeHadoopConf(spark,
      SparkRuleConstants.MRS_WAREHOUSE_HADOOP_NAMESPACE,
      SparkRuleConstants.MRS_WAREHOUSE_HADOOP_NN1,
      SparkRuleConstants.MRS_WAREHOUSE_HADOOP_NN1_ADDR,
      SparkRuleConstants.MRS_WAREHOUSE_HADOOP_NN2,
      SparkRuleConstants.MRS_WAREHOUSE_HADOOP_NN2_ADDR)

    logger.warn(s"start read data from path in orc ...")
    dwDataFrame = spark.read.parquet(SparkRuleConstants.PATH_RULE.replaceAll("tableName", tableName))
    //dwDataFrame = spark.read.parquet(SparkRuleConstants.PATH_RULE.replaceAll("tableName", tableName))
    val columnsInDw = dwDataFrame.schema.map(_.name)
    logger.warn(s"dw DataFrame schema columns: ${columnsInDw}")

    val srcCount = dwDataFrame.count()
    logger.warn(s"data warehouse count in ${tableName}: ${srcCount}")

    logger.warn("start read data from mrs hive ...")
    mrsDataFrame = spark.sql(s"SELECT * FROM ${tableName} WHERE dt='${SparkRuleConstants.LATEST_PARTITION}'")
    val destCount = mrsDataFrame.count()
    logger.warn(s"mrs warehouse count in ${tableName}: ${destCount}")

    columns = mrsDataFrame.schema.map(_.name).filter(!"dt".equals(_))
    logger.warn(s"mrs DataFrame schema columns: ${columns}")

    logger.warn(s"${tableName}双跑情况下数据量情况,srcCount=${srcCount}, destCount=${destCount}")

    if (srcCount != destCount) {
      logger.error(s"${tableName}双跑情况下数据量级不一致,srcCount=${srcCount}, destCount=${destCount}")
    }
  }

  /**
   * primary key
   * 0
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
    logger.warn("-------------------------------------------")
    val cols: Seq[String] = dwDataFrame.schema.map(_.name)

    for (index <- cols.indices) {
      dwDataFrame = dwDataFrame.withColumnRenamed(cols(index), columns(index))
    }
    columns = columns.filter((column: String) =>
      !"load_job_number".equals(column) &&
        !"load_job_name".equals(column) &&
        !"insert_timestamp".equals(column))

    // 排除属于正常数据异常的字段进行明细验证
    columns = columns.filter((column: String) =>
      !"project_manager_list".equals(column) &&
        !"team_name_list".equals(column))

    // 排除掉因为between and使用date数据类型时出现的异常数据
    columns = columns.filter((column: String) =>
      !"agent_office_service_visit_7d_increase_agent_number".equals(column) &&
      !"agent_office_service_visit_7d_record_agent_number".equals(column) &&
      !"agent_office_service_visit_7d_guide_agent_number".equals(column) &&
      !"agent_office_service_visit_mtd_increase_agent_number".equals(column) &&
      !"agent_office_service_visit_mtd_record_agent_number".equals(column) &&
      !"agent_office_service_visit_mtd_guide_agent_number".equals(column) &&
      !"agent_office_service_visit_7d_record_store_number".equals(column) &&
      !"agent_office_service_visit_7d_guide_store_number".equals(column) &&
      !"agent_office_service_visit_mtd_record_store_number".equals(column) &&
      !"agent_office_service_visit_mtd_guide_store_number".equals(column))

    mrsDataFrame.printSchema()
    dwDataFrame.printSchema()

    val result = dwDataFrame.join(mrsDataFrame, columns, "inner")
    //   val result = dwDataFrame.join(mrsDataFrame, columns)
    val bothCount = result.count()
    // TODO 需要将不一致的数据打印到日志中-可以设置batchSizeThreshold，超过不打印
    logger.warn(s"both exist count in ${tableName}: ${bothCount}")
    logger.warn("-------------------------------------------")

    // 打印差集数据的信息
    mrsDataFrame.join(dwDataFrame, columns, "leftanti").show(10000)
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
