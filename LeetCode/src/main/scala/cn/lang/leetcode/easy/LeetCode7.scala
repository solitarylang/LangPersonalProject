package cn.lang.leetcode.easy


object LeetCode7 {
  def main(args: Array[String]): Unit = {
    println(reverse(-123))
    println(reverse(Int.MaxValue))
    println(reverse(Int.MinValue))
  }

  def reverse(x: Int): Int = {
    // 之前直接使用递归的时候出现了901翻转后结果是19的错误结果
    // 这里尝试将整数的翻转转化为字符换的翻转
    if (-10 < x && x < 10) {
      x
    } else {

      var result: String = ""
      var input = 0

      if (x > 0) input = x
      else input = 0 - x;result += "-"

      while (input != 0) {
        result += input % 10
        input = input / 10
      }

      try {
        result.toInt
      } catch {
        case _: Exception => 0
      }
    }

  }
}
