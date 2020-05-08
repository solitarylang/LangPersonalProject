package cn.lang.leetcode.middle

/**
 * 最大正方形
 * 在一个由 0 和 1 组成的二维矩阵内，找到只包含 1 的最大正方形，并返回其面积。
 * 示例:
 * 输入:
 *
 * 1 0 1 0 0
 * 1 0 1 1 1
 * 1 1 1 1 1
 * 1 0 0 1 0
 *
 * 输出: 4
 */
object LeetCode221 {

  def maximalSquare(matrix: Array[Array[Char]]): Int = {
    /**
     * 动态规划的思路实质上就是思考迭代公式
     * 这里使用dp(i)(j)表示以(i,j) 为右下角，且只包含 1 的正方形的边长最大值，最终的结果就是dp()()的最大值
     * 如上述距离矩阵的dp()()矩阵为：
     * 1 1 1 1 1
     * 1 1 1 1 1
     * 1 1 1 2 2
     * 1 1 1 2 2
     */
    var maxSideLength = 0

    if (matrix == null || matrix.length == 0 || matrix(0).length == 0) {
      return maxSideLength;
    }

    val dp = Array.ofDim[Int](matrix.length, matrix(0).length)

    for (i <- matrix.indices) {
      for (j <- matrix(i).indices) {
        // 我们所求的是只包含1的最大正方形,所以可不比管'0'时候的情形
        if (matrix(i)(j) == '1') {
          if (i == 0 || j == 0) {
            dp(i)(j) = 1
          }
          else {
            dp(i)(j) = (Math.min(Math.min(dp(i - 1)(j - 1), dp(i - 1)(j)), dp(i)(j - 1)) + 1)
          }
          print(dp(i)(j) + ":")
          // 每一次遍历求出最大的边长
          maxSideLength = Math.max(maxSideLength, dp(i)(j))
        }
      }
      println("")
    }
    maxSideLength * maxSideLength
  }

  def main(args: Array[String]): Unit = {
    val matrix = Array.ofDim[Char](4, 5)
    //      [["1","0","1","0","0"],["1","0","1","1","1"],["1","1","1","1","1"],["1","0","0","1","0"]]
    matrix(0) = Array[Char]('1', '0', '1', '0', '0')
    matrix(1) = Array[Char]('1', '0', '1', '1', '1')
    matrix(2) = Array[Char]('1', '1', '1', '1', '1')
    matrix(3) = Array[Char]('1', '0', '0', '1', '0')

    println(this.maximalSquare(matrix))
  }
}
