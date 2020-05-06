package cn.lang.scala.function

import scala.beans.BeanProperty

/**
 * @author ：jianglang
 * @date ：Created in 2020/5/6 11:26 AM
 * @description ：使用动态混入方式给对象混入特质，扩充目标类功能
 * @version ：1.0.0
 */
object DynamicMix {
  def main(args: Array[String]): Unit = {
    // 创建对象的时候通过动态混入工作特质，使对象拥有了赚钱的方法
    val xiaoming = new Worker(1) with Business {
      override def earnMoney: Unit = {
        println(s"earn money and my age is: ${age_}")
      }
    }
    xiaoming.earnMoney
  }
}
//
class Worker(age: Int) extends Person {
  @BeanProperty var age_ = age

  def goToOffice() {
    println("go to office")
  }

  override def breath: Unit = {
    println("breath")
  }
}
// 人类特质，有呼吸的方法
trait Person {
  def breath
}
// 工作特质，有赚钱的方法
trait Business {
  def earnMoney
}