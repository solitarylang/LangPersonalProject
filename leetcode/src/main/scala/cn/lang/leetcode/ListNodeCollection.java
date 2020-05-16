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
     * 检查链表是否有环
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

    /**
     * 142
     * 地址： https://leetcode-cn.com/problems/linked-list-cycle-ii/
     * 检查链表是否有环，有环将入环节点返回
     * tip： 不使用额外空间
     */
    public ListNode detectCycle(ListNode head) {
        /** 使用双指针的方式检查是否有环 */
        boolean hasCycle = false;
        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;

            if (slow == fast) {
                hasCycle = true;
                break;
            }
        }

        // 如果有环，那么继续求出入环节点，注意闭环等特殊情况验证
        if (hasCycle) {
            if (head == slow) {
                return slow;
            }
            // 有环的话: 2*(s1+s2)=s1+s2+n*s3 => s1+s2=n*s3
            // s1 = distance(head->入环点)
            // s2 = distance(入环点->首次相遇点)
            // s3 = length(环)
            // 那么由已知条件： s1=n*s3-s2=s3-s2+(n-1)*s3
            // 其中(s3-s2)是首次相遇点到入环点的距离
            while (head.next != slow.next) {
                head = head.next;
                slow = slow.next;
            }
        } else {
            // 无环直接返回空
            return null;
        }
        return slow.next;
    }

    public static void main(String[] args) {
        ListNodeCollection bean = new ListNodeCollection();

        ListNode head = new ListNode(3);
        ListNode second = new ListNode(2);
        ListNode third = new ListNode(0);
        ListNode fourth = new ListNode(-4);

        head.next = second;
        second.next = third;
        third.next = fourth;
        fourth.next = second;

        System.out.println(bean.detectCycle(head).val);
    }
}
