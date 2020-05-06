package cn.lang.leetcode.easy

/**
 * 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
 * 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
 */
object LeetCode70 {
  def main(args: Array[String]): Unit = {
    println(climbStairs(1))
    println(climbStairs(4))
    println(climbStairs(10))
  }

  def climbStairs(n: Int): Int = {
    // 这其实是斐波那契数列，可以根据求解的通向公式计算
    ((Math.pow((1 + Math.sqrt(5D)) / 2, n + 1) - Math.pow((1 - Math.sqrt(5D)) / 2, n + 1)) / Math.sqrt(5D)).toInt
  }
}
