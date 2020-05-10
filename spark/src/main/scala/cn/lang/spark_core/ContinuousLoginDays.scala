package cn.lang.spark_core

import java.text.{ParseException, SimpleDateFormat}
import java.util.Calendar

import org.apache.spark.sql.SparkSession

object ContinuousLoginDays {
  def main(args: Array[String]): Unit = {
    // env
    val spark: SparkSession = SparkSession
      .builder()
      .appName("ContinuousLoginDays")
      .master("local[*]")
      .getOrCreate()
    val sc = spark.sparkContext
    // source,可以是load hive(开启hive支持)或者parquet列式文件(定义好schema)
    val source = sc.textFile("/user/hive/warehouse/dwd/login_log")

    case class Login(uid: Int, loginTime: String) // 可以kryo序列化

    /** get date last `abs(n)` days defore or after biz_date   *
     * example biz_date = 20200101 ,last_n = 1,return 20191231 */
    def getLastNDate(biz_date: String,
                     date_format: String = "yyyyMMdd",
                     last_n: Int = 1): String = {
      val calendar: Calendar = Calendar.getInstance()
      val sdf = new SimpleDateFormat(date_format)
      try
        calendar.setTime(sdf.parse(biz_date))
      catch {
        case e: ParseException => // omit
      }
      calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - last_n)
      sdf.format(calendar.getTime)
    }

    // transform
    val result = source
      .map(_.split("\t"))
      .map(iterm => Login(iterm(0).toInt, iterm(1)))
      .groupBy(_.uid) // RDD[(Int, Iterable[Login])]
      .map(iterm => {
        // 用于给此uid标记是否符合要求
        var CONTINUOUS_LOGIN_N = false

        val logins = iterm._2
          .toSeq
          .sortWith((v1, v2) => v1.loginTime.compareTo(v2.loginTime) > 0)

        var lastLoginTime: String = ""
        var loginDays: Int = 0

        logins
          .foreach(iterm => {
            if (lastLoginTime == "") {
              lastLoginTime = iterm.loginTime
              loginDays = 1
            } else if (getLastNDate(iterm.loginTime) == lastLoginTime) {
              lastLoginTime = iterm.loginTime
              loginDays = 2
            } else {
              lastLoginTime = iterm.loginTime
              loginDays = 1
            }
          })

        if (loginDays > 3) CONTINUOUS_LOGIN_N = true

        /** 此处可以使用集合将连续登陆的情况保留，
         * 也可以直接按照是否连续登陆N天进行标记
         */
        (iterm._1, CONTINUOUS_LOGIN_N)
      })
      .filter(_._2)
      .map(_._1)
    // sink
    result.foreach(println(_))
  }
}
