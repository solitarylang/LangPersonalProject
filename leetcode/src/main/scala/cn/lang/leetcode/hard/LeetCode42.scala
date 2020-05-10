package cn.lang.leetcode.hard

/**
 * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
 *
 * 上面是由数组 [0,1,0,2,1,0,1,3,2,1,2,1] 表示的高度图，在这种情况下，可以接 6 个单位的雨水（蓝色部分表示雨水）
 *
 * 示例:
 *
 * 输入: [0,1,0,2,1,0,1,3,2,1,2,1]
 * 输出: 6
 */
object LeetCode42 {
  /**
   * 动态规划：将复杂问题简化成方便计算的单元
   * 1.选取单元 {{{head:::middle:::tail}}} ,其中middle集合中所有元素都 <= min(head,tail)
   * 2.单元蓄水的量级容易计算 = sum(min(head,tail) - iterm)
   * 3.上面的思路无法计算螺旋递减的情况，即不完全蓄水池
   * 4.细化，从左遍历的时候出现，记录左高点，在下一次出现上升时划分单元
   * 5.上面的思路细化了单元，但是未解决打单员包裹小单元出现的遗漏计算
   * 6.所以需要两个索引从左到右和从右到左去记录两个方向高点位置
   */
  def trap(height: Array[Int]): Int = {
    if (height == null || height.length <= 1) return 0
    // 初始化
    var result = 0
    val length = height.length
    // 截止目前最高位左右两个方向看
    val (left, right) = (Array.ofDim[Int](length), Array.ofDim[Int](length))

    left(0) = height(0)
    for (i <- height.indices if i != 0) left(i) = Math.max(height(i), left(i - 1))

    right(length - 1) = height(length - 1)
    for (j <- height.indices if j != 0) right(length - 1 - j) = Math.max(height(length - 1 - j), right(length - j))

    // 开始按照每一个点计算可能蓄水值
    for (k <- height.indices) result += Math.min(left(k), right(k)) - height(k)

    // return result
    result
  }

  def main(args: Array[String]): Unit = {
    val input1 = Array[Int](0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1)
    println(trap(input1))

    val input2 = Array[Int]()
    println(trap(input2))

    val input3 = Array[Int](0, 1, 2, 3, 2, 1, 0)
    println(trap(input3))
  }
}
