package cn.lang.leetcode.easy

/**
 * 给定一个链表，判断链表中是否有环。
 *
 * 为了表示给定链表中的环，我们使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。 如果 pos 是 -1，则在该链表中没有环。
 */
object LeetCode141 {

  class ListNode(var _x: Int = 0) {
    var next: ListNode = null
    var x: Int = _x
  }

  def hasCycle(head: ListNode): Boolean = {
    // hash表，核心思想是每一个节点的地址是不会更改的
    val sites = new scala.collection.mutable.ListBuffer[ListNode] // 存储遍历到每一个节点的地址

    var input = head
    while (input != null) {
      if (sites.contains(input)) return true
      else sites.append(input);input = input.next
    }
    false
  }

  def main(args: Array[String]): Unit = {
    val head = new ListNode(3)
    val second = new ListNode(2)
    val third = new ListNode(0)
    val fourth = new ListNode(-4)

    head.next = second
    second.next = third
    third.next = fourth
    fourth.next = second

    println(hasCycle(head))
  }
}
