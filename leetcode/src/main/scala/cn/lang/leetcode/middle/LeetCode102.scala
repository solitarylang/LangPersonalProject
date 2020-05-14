package cn.lang.leetcode.middle

/**
 * 给你一个二叉树，请你返回其按 层序遍历 得到的节点值。 （即逐层地，从左到右访问所有节点）。
 *
 * 示例：
 * 二叉树：[3,9,20,null,null,15,7],
 *
 * 3
 * / \
 * 9  20
 * /  \
 * 15   7
 * 返回其层次遍历结果：
 *
 * [
 * [3],
 * [9,20],
 * [15,7]
 * ]
 */
class TreeNode(var _value: Int) {
  var value: Int = _value
  var left: TreeNode = null
  var right: TreeNode = null
}

class LeetCode102 {
  /**
   * 思路1： 将所有节点按照层数放在集合集合中，再遍历依次取出
   * 思路2： 使用两个队列，保存父节点和子节点，然后依次取出
   */
  def levelOrder(root: TreeNode): List[List[Int]] = {
    // 临界条件
    if (root == null) return List[List[Int]]()
    // 结果
    var result = new scala.collection.mutable.ListBuffer[List[Int]]
    // 使用两个队列来保存父节点和子节点
    // 也可以使用一个队列来保存，用默认的TreeNode作为分界符来区分树的层次
    // 也可以使用一个队列，使用循环来区分树的层次
    var nodes = new scala.collection.mutable.Queue[TreeNode]
    nodes.enqueue(root)
    result.append(List(root.value))

    while (nodes.nonEmpty) {
      val rollTimes = nodes.size
      val layer = new scala.collection.mutable.ListBuffer[Int]

      // 开始对每一层进行遍历
      for (_ <- 0 until rollTimes) {
        val tmp = nodes.dequeue()

        if (tmp.left != null) {
          // 如果存在左节点，需要将节点值添加到对应层结果中
          layer.append(tmp.left.value)
          // 并将节点添加到节点队列
          nodes.enqueue(tmp.left)
        }

        if (tmp.right != null) {
          layer.append(tmp.right.value)
          nodes.enqueue(tmp.right)
        }
        // 如果没有左右节点
        // do nothing
      }

      // 循环完毕需要将每一次的结果放回结果集中
      if (layer.nonEmpty) {
        result.append(layer.toList)
      }
    }
    result.toList
  }
}

object LeetCode102 {
  def main(args: Array[String]): Unit = {
    val root = new TreeNode(3)
    val left = new TreeNode(9)
    val right = new TreeNode(20)
    val right_left = new TreeNode(15)
    val right_right = new TreeNode(7)

    root.left = left
    root.right = right
    right.left = right_left
    right.right = right_right

    val bean = new LeetCode102

    val result = bean.levelOrder(root)

    println(result)
  }
}

