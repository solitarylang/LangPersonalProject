package cn.lang.leetcode;

import java.util.HashSet;

/**
 * @author ：jianglang
 * @date ：Created in 2020/5/16 5:49 PM
 * @description：使用java处理链表相关的leetcode题目
 * @version: 1.0.0$
 */
public class ListNodeCollection {
    /**
     * 单链表节点单元
     */
    private static class ListNode {
        int val;
        ListNode next;

        ListNode(int _val) {
            this.val = _val;
            this.next = null;
        }

    }

    /**
     * 141
     * 地址： https://leetcode-cn.com/problems/linked-list-cycle/
     */
    private boolean hasCycle(ListNode head) {
//        // 使用集合保存节点，使用节点地址唯一的条件
//        HashSet<ListNode> listNodes = new HashSet<ListNode>();
//
//        while (head != null) {
//            if (listNodes.contains(head)) {
//                return true;
//            } else {
//                listNodes.add(head);
//                head = head.next;
//            }
//        }
//        return false;

        /** 使用双指针的方式 */
        if (head == null || head.next == null) {
            return false;
        }

        ListNode slow = head;
        ListNode fast = head.next;

        // 如果出现环，那么双指针一定会出现相等的情况
        while (slow != fast) {
            if (fast == null || fast.next == null) {
                // 这个条件是如果没有环，2步长的快指针已经到达了链表的末端
                return false;
            }

            slow = slow.next;
            fast = fast.next.next;
        }

        return true;
    }

    public static void main(String[] args) {

    }
}
