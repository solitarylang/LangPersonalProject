package cn.lang.scala

import scala.annotation.tailrec

/**
 * 用于记录scala尾递归的一些笔记
 */
object TailRecursive {
//  @tailrec
  def foo(num: Int): Long = {
    if (num <= 1) num.toLong
    else num + foo(num - 1)
  }

  def bar(num: Int): Long = {
    var acc = 0L;
    for (i <- 1 to num) { acc += i }
    acc
  }

  def bee(num: Int): Long = {
    @tailrec
    def bee(num: Int, acc: Long): Long = {
      if (num < 1) -1
      else if (num == 1) acc
      else bee(num - 1, acc + num)
    }

    bee(num, 1)
  }

  def main(args: Array[String]): Unit = {
    //    println(foo(10))
    //    println(foo(100))
    println(foo(100000))
    println(bee(100000))
  }
}
