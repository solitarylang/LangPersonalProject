package cn.lang.leetcode.easy

/**
 * 实现 int sqrt(int x) 函数。
 * 计算并返回 x 的平方根，其中 x 是 ` 非负整数 ` 。
 * 由于返回类型是整数，结果只保留整数的部分，小数部分将被舍去。
 *
 * 示例 1:
 * 输入: 4
 * 输出: 2
 *
 * 示例 2:
 * 输入: 8
 * 输出: 2
 * 说明: 8 的平方根是 2.82842...,
 *      由于返回类型是整数，小数部分将被舍去。
 */
object LeetCode69 {
  def mySqrt(x: Int): Int = {
    /* 采用最容易想到的二分法进行逼近 */
    // left和right用于记录左右的界限
    var left = 0
    var right = x
    var result = -1
    while (left <= right) {
      val middle = left + (right - left) / 2 // 先减后加可以避免溢出，这里不影响
      // 这里middle * middle会溢出Int
      if (middle * middle.toLong <= x) {
        // 因为是保留整数，所以考虑左节点即可
        result = middle
        left = middle + 1
      } else {
        right = middle - 1
      }
    }
    result
  }

  def main(args: Array[String]): Unit = {
    println(mySqrt(0))
    println(mySqrt(1))
    println(mySqrt(8))
    println(mySqrt(10))
    println(mySqrt(36))
    println(mySqrt(2147395599))
  }
}
