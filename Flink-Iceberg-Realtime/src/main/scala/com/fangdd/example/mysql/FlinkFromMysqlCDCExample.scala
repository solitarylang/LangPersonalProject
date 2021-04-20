package com.fangdd.example.mysql

import com.alibaba.ververica.cdc.connectors.mysql.MySQLSource
import com.alibaba.ververica.cdc.debezium.StringDebeziumDeserializationSchema
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.bridge.scala._
import org.apache.flink.types.Row

/**
 * 使用Flink消费mysql change log的使用样例
 * <link> https://github.com/ververica/flink-cdc-connectors/wiki
 *
 * @author lang
 * @since 4/15/21 9:52 AM
 */
class FlinkFromMysqlCDCExample {
  /**
   * 本地mysql读取CDC数据并打印
   */
  def printCDCFromLocal(): Unit = {
    val sourceFunction: SourceFunction[String] = MySQLSource.builder[String]
      .hostname("localhost")
      .port(3306)
      .databaseList("test")
      .username("root") // monitor all tables under inventory database
      .password("root")
      .deserializer(new StringDebeziumDeserializationSchema)
      .build // converts SourceRecord to String

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.addSource(sourceFunction)
      .print
      .setParallelism(1) // use parallelism 1 for sink to keep message ordering

    env.execute("printCDC_test")
  }

  def printCDC(): Unit = {
    val sourceFunction: SourceFunction[String] = MySQLSource.builder[String]
      .hostname("10.50.255.60")
      .port(3306)
      .databaseList("test")
      .username("bigdata") // monitor all tables under inventory database
      .password("bigdata")
      .deserializer(new StringDebeziumDeserializationSchema)
      .build // converts SourceRecord to String

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.addSource(sourceFunction)
      .print
      .setParallelism(1) // use parallelism 1 for sink to keep message ordering

    env.execute("printCDC_test")
  }

  /**
   * 从本地mysql读取CDC数据
   * <link> https://cloud.tencent.com/developer/article/1698639
   */
  def printCDCWithSqlFromLocal(): Unit = {
    val createTableStatement = "-- creates a mysql cdc table source\n" +
      "CREATE TABLE runoob_tbl (\n" +
      " runoob_id INT NOT NULL,\n" +
      " runoob_title STRING,\n" +
      " runoob_author STRING,\n" +
      " submission_date Date\n" +
      ") WITH (\n" +
      " 'connector' = 'mysql-cdc',\n" +
      " 'hostname' = 'localhost',\n" +
      " 'port' = '3306',\n" +
      " 'username' = 'root',\n" +
      " 'password' = 'root',\n" +
      " 'database-name' = 'test',\n" +
      " 'table-name' = 'runoob_tbl'\n" +
      ")"

    val queryTableStatement = "-- read snapshot and binlog data from mysql, and do some transformation, and show on the client\n" +
      "SELECT runoob_id, runoob_title, runoob_author, submission_date FROM runoob_tbl"

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val stEnv = StreamTableEnvironment.create(env)

    stEnv.executeSql(createTableStatement)

    val table = stEnv.sqlQuery(queryTableStatement)
    // implicit
    implicit val typeInfo = TypeInformation.of(classOf[Row])

    val rowDataStream = stEnv.toRetractStream(table)

    rowDataStream.print()

    env.execute("printCDCWithSqlFromLocal_test")
  }

  def printCDCWithSql(): Unit = {
    val createTableStatement = "-- creates a mysql cdc table source\n" +
      "CREATE TABLE tea_stu (\n" +
      " tid INT NOT NULL,\n" +
      " t_name STRING,\n" +
      " s_name STRING\n" +
      ") WITH (\n" +
      " 'connector' = 'mysql-cdc',\n" +
      " 'hostname' = '10.50.255.60',\n" +
      " 'port' = '3306',\n" +
      " 'username' = 'bigdata',\n" +
      " 'password' = 'bigdata',\n" +
      " 'database-name' = 'test',\n" +
      " 'table-name' = 'tea_stu'\n" +
      ");"

    val queryTableStatement = "-- read snapshot and binlog data from mysql, and do some transformation, and show on the client\n" +
      "SELECT tid, t_name, s_name FROM tea_stu;"

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.execute("printCDCWithSql_test")
  }
}
