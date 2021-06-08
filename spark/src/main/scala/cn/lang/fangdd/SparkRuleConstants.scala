package cn.lang.fangdd

/**
 * @author lang
 * @since 4/7/21 2:06 PM
 */
object SparkRuleConstants {
  /**
   * GENERAL
   */
  val PATH_RULE: String = "hdfs://hacluster/tmp/compare/tableName"

  val LATEST_PARTITION: String = "20210412"

  /**
   * DATA
   */
  val DATA_WAREHOUSE_HIVE_META_STORE_URI = "thrift://10.50.23.212:9083,thrift://10.50.23.216:9083"

  val DATA_WAREHOUSE_HADOOP_NAMESPACE = "fddhdfsmaster"
  // m-hadoop.esf.fdd:8020
  val DATA_WAREHOUSE_HADOOP_NN1_ADDR = "10.50.23.210:8020"
  val DATA_WAREHOUSE_HADOOP_NN1 = "nn1"
  // w9-hadoop.esf.fdd:8020
  val DATA_WAREHOUSE_HADOOP_NN2_ADDR = "10.50.23.216:8020"
  val DATA_WAREHOUSE_HADOOP_NN2 = "nn2"

  /**
   * MRS
   */
  val MRS_WAREHOUSE_HIVE_META_STORE_URI = "thrift://10.100.86.119:9083,thrift://10.100.193.155:9083"

  val MRS_WAREHOUSE_HADOOP_NAMESPACE = "hacluster"
  val MRS_WAREHOUSE_HADOOP_NN1 = "3"
  val MRS_WAREHOUSE_HADOOP_NN1_ADDR = "10.100.193.155:8020"
  val MRS_WAREHOUSE_HADOOP_NN2 = "4"
  val MRS_WAREHOUSE_HADOOP_NN2_ADDR = "10.100.86.119:8020"

  /**
   * NEED
   */
  val NEED_COMPARE_TABLES: List[String] = List(
    "edw_public.dim_edw_pub_city_type_flag_info"
    , "edw_reports.adm_edw_agents_growth_center_daily_report_010_di"
    , "edw_reports.adm_edw_agents_growth_center_daily_report_009_di"
    , "edw_reports.adm_edw_city_agent_share_usre_activation_daily_statistic_df"
    , "edw_reports.adm_xf_edw_house_sub_project_report_004_daily_di"
    , "edw_reports.adm_xf_edw_organization_agent_report_001_daily_di"
    , "edw_reports.adm_xf_edw_agents_office_service_daily_reports_001_di"
    , "edw_reports.adm_xf_edw_house_sub_project_report_005_daily_di"
    , "edw_reports.adm_xf_edw_house_sub_project_report_003_daily_di"
    , "edw_reports.adm_xf_edw_house_sub_project_report_001_daily_di"
    , "edw_houses.adm_xf_edw_house_sub_project_edw_001_df"
    , "edw_reports.adm_xf_edw_house_sub_project_report_006_daily_di"
    , "edw_reports.adm_xf_edw_organization_visit_agent_office_daily_reports_001_di"
    , "edw_reports.adm_xf_edw_house_sub_project_report_002_daily_di"
    , "edw_reports.adm_xf_edw_agents_office_service_daily_reports_002_di"
    , "edw_agents.adm_edw_xf_business_staff_operation_di")

  val NEED_COMPARE_TABLES_IN_PARQUET: List[String] = List(
    "adm.house_project_directselling_staff_performance_statistic_daily_df"
    , "adm.house_project_directselling_agent_activity_performance_statistic_daily_df"
    , "adm.house_project_directselling_project_assistant_statistic_daily_df")
}
