package com.fangdd.example.mysql

import org.scalatest.{BeforeAndAfter, FunSuite}

/**
 * @author lang
 * @since 4/15/21 5:32 PM
 */
class FlinkFromMysqlCDCExampleTest extends FunSuite with BeforeAndAfter {

  test("printCDCFromLocal") {
    //    Caused by: java.lang.IllegalStateException:
    //    Cannot read the binlog filename and position via 'SHOW MASTER STATUS'.
    //    Make sure your server is correctly configured
    // Solution:
    //    show variables like 'log-bin'
    //    https://blog.csdn.net/ayqy42602/article/details/108848358

    val example = new FlinkFromMysqlCDCExample
    example.printCDCFromLocal()

    //    SourceRecord {
    //      sourcePartition = {
    //        server = mysql_binlog_source
    //      }, sourceOffset = {
    //        ts_sec = 1618820603,
    //        file = mysql - bin .000001,
    //        pos = 618,
    //        row = 1,
    //        server_id = 1,
    //        event = 2
    //      }
    //    }
    //    ConnectRecord {
    //      topic = 'mysql_binlog_source.test.runoob_tbl', kafkaPartition = null, key = Struct {
    //        runoob_id = 1
    //      }, keySchema = Schema {
    //        mysql_binlog_source.test.runoob_tbl.Key: STRUCT
    //      }, value = Struct {
    //        after = Struct {
    //          runoob_id = 1, runoob_title = 学习 PHP, runoob_author = 菜鸟教程, submission_date = 18736
    //        }, source = Struct {
    //          version = 1.2 .1.Final, connector = mysql, name = mysql_binlog_source, ts_ms = 1618820603000, db = test, table = runoob_tbl, server_id = 1, file = mysql - bin .000001, pos = 758, row = 0, thread = 2
    //        }, op = c, ts_ms = 1618820603526
    //      }, valueSchema = Schema {
    //        mysql_binlog_source.test.runoob_tbl.Envelope: STRUCT
    //      }, timestamp = null, headers = ConnectHeaders(headers = )
    //    }

  }

  test("printCDC") {
    val example = new FlinkFromMysqlCDCExample
    example.printCDC()
  }
  /**
   * 使用sql方式消费本地Mysql的 CDC
   */
  test("printCDCWithSqlFromLocal") {
    val example = new FlinkFromMysqlCDCExample
    example.printCDCWithSqlFromLocal()

    //    7> (true,1,学习 C++,菜鸟教程,2021-04-19)
    //    8> (true,2,学习 PHP,菜鸟教程,2021-04-19)
  }

  test("printCDCWithSql") {
    val example = new FlinkFromMysqlCDCExample
    example.printCDCWithSql()
  }
}
