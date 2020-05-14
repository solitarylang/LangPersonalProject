package cn.lang.leetcode.middle

/**
 * 给定一个单链表，把所有的奇数节点和偶数节点分别排在一起。请注意，这里的奇数节点和偶数节点指的是节点编号的奇偶性，而不是节点的值的奇偶性。
 *
 * 请尝试使用原地算法完成。你的算法的空间复杂度应为 O(1)，时间复杂度应为 O(nodes)，nodes 为节点总数。
 *
 * 示例 1:
 *
 * 输入: 1->2->3->4->5->NULL
 * 输出: 1->3->5->2->4->NULL
 * 示例 2:
 *
 * 输入: 2->1->3->5->6->4->7->NULL
 * 输出: 2->3->6->7->1->5->4->NULL
 * 说明:
 *
 * 应当保持奇数节点和偶数节点的相对顺序。
 * 链表的第一个节点视为奇数节点，第二个节点视为偶数节点，以此类推。
 */
object LeetCode328 {

  class ListNode(var _x: Int = 0) {
    var next: ListNode = null
    var x: Int = _x
  }

  /**
   * 1.空间复杂度为O(1)，所以不能创建类大小的结构(使用原地算法:复用原有结构)
   * 2.时间复杂度为O(n)，所以遍历只能是已知次
   * 3.遍历的时候将原来的拆分成两个单链表，最后结合即可
   * 4.为了防止二次遍历，需要记录奇数的最后一个节点
   */
  def oddEvenList(head: ListNode): ListNode = {
    if (head == null ) return null

    var (odd, even, evenHead) = (head, head.next, head.next)

    while (even != null && even.next != null) {
      odd.next = even.next
      odd = odd.next

      even.next = odd.next
      even = even.next
    }
    odd.next = evenHead
    head
  }

  def main(args: Array[String]): Unit = {

  }
}
