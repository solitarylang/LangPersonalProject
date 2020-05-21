package cn.lang.leetcode.hard

/**
 * 给你一个链表，每 k 个节点一组进行翻转，请你返回翻转后的链表。
 * k 是一个正整数，它的值小于或等于链表的长度。
 * 如果节点总数不是 k 的整数倍，那么请将最后剩余的节点保持原有顺序。
 * 示例：
 * 给你这个链表：1->2->3->4->5
 * 当 k = 2 时，应当返回: 2->1->4->3->5
 * 当 k = 3 时，应当返回: 3->2->1->4->5
 * 说明：
 * 你的算法只能使用常数的额外空间。
 * 你不能只是单纯的改变节点内部的值，而是需要实际进行节点交换。
 */
object LeetCode25 {

  class ListNode(var _x: Int = 0) {
    var next: ListNode = null
    var x: Int = _x
  }

  def reverseKGroup(head: ListNode, k: Int): ListNode = {
    var input = head
    var part: ListNode = null
    var result: ListNode = null
    var index = 1

    while (input.next != null) {
      val next = input.next
      part = next
      part.next = input
      input = next
      index += 1

      // 满足k次循环就将结果放在result上
      if (index == k) {
        result.next = part
        index = 0
      }
    }

    result
  }

  def main(args: Array[String]): Unit = {

  }
}
