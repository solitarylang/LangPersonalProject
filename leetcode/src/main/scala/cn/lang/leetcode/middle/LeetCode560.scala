package cn.lang.leetcode.middle

/**
 * 给定一个整数数组和一个整数 k，你需要找到该数组中和为 k 的连续的子数组的个数。
 *
 * 示例 1 :
 * 输入:nums = [1,1,1], k = 2
 * 输出: 2 , [1,1] 与 [1,1] 为两种不同的情况。
 *
 * 说明 :
 *
 * 数组的长度为 [1, 20,000]。
 * 数组中元素的范围是 [-1000, 1000] ，且整数 k 的范围是 [-1e7, 1e7]。
 */
object LeetCode560 {
  def subarraySum(nums: Array[Int], k: Int): Int = {
    // 考虑使用动态规划的方法
    def subarraySum1(nums: Array[Int], start: Int, end: Int, k: Int): Int = {
      if (start < end) {
        var sum = nums(start) // 为了避免和k少一次比较以及初始值的影响
        var index = start + 1
        var num = if (sum == k) 1 else 0
        while (index <= end) {
          sum += nums(index)
          if (sum == k) num += 1
          index += 1
        }
        subarraySum1(nums, start + 1, end, k) + num
      } else {
        if (nums(end) == k) 1
        else 0
      }
    }

    subarraySum1(nums, 0, nums.length - 1, k)
  }

  def main(args: Array[String]): Unit = {

    val input1 = Array(1, 1, 1)
    val input2 = Array(1, 2, 1, 2, 1)
    val input3 = Array(-1, -1, 1)
    val input4 = Array(100, 1, 2, 3, 4)

    println(subarraySum(input1, 2))
    println(subarraySum(input2, 3))
    println(subarraySum(input3, 0))
    println(subarraySum(input4, 3))

  }
}