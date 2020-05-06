package cn.lang.leetcode.hard

import java.util

/**
 * 给你两个单词 word1 和 word2，请你计算出将 word1 转换成 word2 所使用的最少操作数 。
 * 你可以对一个单词进行如下三种操作：
 *
 * 插入一个字符
 * 删除一个字符
 * 替换一个字符
 * 示例 1：
 *
 * 输入：word1 = "horse", word2 = "ros"
 * 输出：3
 * 解释：
 * horse -> rorse (将 'h' 替换为 'r')
 * rorse -> rose (删除 'r')
 * rose -> ros (删除 'e')
 *
 * https://leetcode-cn.com/problems/edit-distance
 */
object LeetCode74 {
  def main(args: Array[String]): Unit = {
    println(minDistance("source", "rce"))
    println(minDistance("abc", "abcd"))
  }

  def minDistance(source: String, target: String): Int = {
    // 获取转换的两个单词的长度，这里默认都只有单纯的英文字符
    val len_source = source.length
    val len_target = target.length
    // 创建一个二维数组，存储source和target子集转换的最小步数，考虑使用迭代关系
    // 比如steps(i)(j)表示source.subString(0,i+1)转换成source.subString(0,j+1)的最小步数
    val steps = Array.ofDim[Int](len_source + 1, len_target + 1)

    /* 这里是初始化，很明显可以得到以下结论 */
    for (i <- 0 to len_source) {
      steps(i)(0) = i
    }

    for (j <- 0 to len_target) {
      steps(0)(j) = j
    }

    for (i <- 1 to len_source) {
      for (j <- 1 to len_target) {
        // steps(i-1)(j-1)代表 [?] -> [??]  的最小步数,A
        // steps(i)(j-1)代表 [?]X -> [??]  的最小步数,B
        // steps(i-1)(j)代表 [?] -> [??]Y  的最小步数,C
        // steps(i)(j)代表 [?]X -> [??]Y  的最小步数,D
        // 小循环是B->D,大循环是C->D
        if (source.charAt(i - 1) == target.charAt(j - 1)) { // 如果上述X和Y相等，那么必然就有如下的结果
          steps(i)(j) = steps(i - 1)(j - 1)
        } else { // 如果XY不相等
          steps(i)(j) = 1 + Math.min(steps(i - 1)(j - 1), Math.min(steps(i - 1)(j), steps(i)(j - 1)))
        }
      }
    }
    steps(len_source)(len_target)
  }
}
