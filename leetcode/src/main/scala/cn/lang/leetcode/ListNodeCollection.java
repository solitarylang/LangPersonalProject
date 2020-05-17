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

    /**
     * 160
     * 地址： https://leetcode-cn.com/problems/intersection-of-two-linked-lists/
     * 找到两个单链表相交的起点
     * tip： o(1)内存
     */
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        // 核心理念是双指针分别遍历A+B和B+A:
        // 1.首先能确定两次遍历会同时结束，因为长度一样
        // 2.我们如此划分：A=XA+AB，B=XB+AB，如果A和B相交，那么AB不为空,AB的投即为所求
        // 3.那么两次遍历路程分别为：
        //      XA+AB+XB+AB
        //      XB+AB+XA+AB
        // 4.所以两次遍历，首次出现相同的相同节点即为所求，可能在第一个AB，也可能在第二个AB(更多可能)
        if (headA == null || headB == null) return null;
        ListNode pointerA = headA, pointerB = headB;

        // 不用刻意去考虑循环结束的条件，因为最终都会走完，最终的结果都是null(如果没有相交)
        while (pointerA != pointerB) {
            pointerA = pointerA == null ? headB : pointerA.next;
            pointerB = pointerB == null ? headA : pointerB.next;
        }

        return pointerA;
    }

    /**
     * 19
     * 地址： https://leetcode-cn.com/problems/remove-nth-node-from-end-of-list/
     * 删除链表中倒数第n个节点，然后返回头结点
     * tip： 保证n有效，能使用一趟扫描实现吗？
     */
    public ListNode removeNthFromEnd(ListNode head, int n) {
        // 联想到单链表只能从前往后遍历，所以关键问题是如何找出倒数第n个节点的前驱结点
        // 核心理念是采用双指针：
        // 1.A和B相差n步长从前往后遍历,当B节点到达链表最后时，A节点即为倒数第n个节点的前驱节点
        // n1(A) n2 n3(B) n4 n5 n6 n7
        // n1 n2 n3 n4 n5(A) n6 n7(B)
        // 2.考虑到如果倒数第n个节点刚好是头结点，那么它的前驱结点是不存在的，可以赋一个哑结点n0
        // n0(A) n1 n2(B) n3 n4 n5 n6 n7
        // n0 n1 n2 n3 n4 n5(A) n6 n7(B)
        ListNode n0 = new ListNode(0);// 哑结点,赋值不影响结果
        n0.next = head;
        ListNode A = n0;
        ListNode B = n0;
        for (int i = 1; i <= n + 1; i++) {
            B = B.next;
        }
        while (B != null) {
            A = A.next;
            B = B.next;
        }
        A.next = A.next.next;
        return n0.next;
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
