package cn.lang.scala.utils

import java.text.{ParseException, SimpleDateFormat}
import java.util.Calendar

import org.slf4j.{Logger, LoggerFactory}

/**
 * @author ：jianglang
 * @date ：Created in 2020/3/6 12:50 PM
 * @description ：utils related with date
 * @version ：1.0.0
 */
object DateUtil {

  private val logger: Logger = LoggerFactory.getLogger("cn.lang.spark.module.utils.DateUtil")

  /* ------------------------- *
   |   Days-Transfer methods   |
   * ------------------------- */

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
      case e: ParseException => logger.error(e.getMessage)
    }
    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - last_n)
    sdf.format(calendar.getTime)
  }

  /* get yesterday */
  def getYesterDay(biz_date: String, date_format: String): String = {
    getLastNDate(biz_date, date_format, 1)
  }

  //TODO 获取指定格式日期的指定前几天

  //TODO 用上述方法写一个重载方法来获取昨天

  //TODO 日期转换成时间戳的方法

  //TODO 将指定日期归一化到当天的凌晨零点

  //TODO 将指定日期归一化到当周的凌晨零点

  //TODO 将指定日期归一化到当月的凌晨零点
}
