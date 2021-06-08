package cn.lang.fangdd

import org.apache.hadoop.fs.Path
import org.apache.spark.sql.SparkSession

/**
 * @author lang
 * @since 5/17/21 3:01 PM
 */
object Test {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("compare_data")
      .config("hive.metastore.uris", SparkRuleConstants.DATA_WAREHOUSE_HIVE_META_STORE_URI)
      .enableHiveSupport()
      .master("local[*]")
      .getOrCreate()

    val tableName = "edw_public.dim_edw_pub_city_type_flag_info"

    HadoopConf.changeHadoopConf(spark,
      SparkRuleConstants.DATA_WAREHOUSE_HADOOP_NAMESPACE,
      SparkRuleConstants.DATA_WAREHOUSE_HADOOP_NN1,
      SparkRuleConstants.DATA_WAREHOUSE_HADOOP_NN1_ADDR,
      SparkRuleConstants.DATA_WAREHOUSE_HADOOP_NN2,
      SparkRuleConstants.DATA_WAREHOUSE_HADOOP_NN2_ADDR)

    val df1 = spark.sql(s"SELECT * FROM ${tableName} WHERE dt='${SparkRuleConstants.LATEST_PARTITION}'")

    df1.printSchema()

//    spark.sparkContext.hadoopConfiguration.addResource(new Path("/Users/langjiang/Ideaproject/Github/LangPersonalProject/spark/src/main/resources/dw/hdfs-site.xml"))
//    spark.sparkContext.hadoopConfiguration.addResource(new Path("/Users/langjiang/Ideaproject/Github/LangPersonalProject/spark/src/main/resources/dw/core-site.xml"))

    val df2 = spark.read.orc(
      s"hdfs://fddhdfsmaster/user/hive/warehouse/edw_public.db/dim_edw_pub_city_type_flag_info/dt=${SparkRuleConstants.LATEST_PARTITION}")

    df2.printSchema()
  }
}
