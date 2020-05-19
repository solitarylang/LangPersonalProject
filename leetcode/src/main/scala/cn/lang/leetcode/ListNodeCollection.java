package cn.lang.leetcode;

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
        // 1.A和B相差n(例子：n取1)步长从前往后遍历,当B节点到达链表最后时，A节点即为倒数第n个节点的前驱节点
        // n1(A) n2 n3(B) n4 n5 n6 n7 null
        // n1 n2 n3 n4 n5 n6(A) n7 null(B)
        // 2.考虑到如果倒数第n个节点刚好是头结点，那么它的前驱结点是不存在的，可以赋一个哑结点n0
        // n0(A) n1 n2(B) n3 n4 n5 n6 n7 null
        // n0 n1 n2 n3 n4 n5 n6(A) n7 null(B)
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

    /**
     * 24 / 206
     * 地址： https://leetcode-cn.com/problems/fan-zhuan-lian-biao-lcof/
     * 反转链表
     */
    public ListNode reverseList(ListNode head) {
        // 局部反转后的链表
        ListNode result = null;
        while (head != null) {
            // 局部反转，当然也可以用递归的形式
            ListNode tmp = head.next;

            head.next = result;
            result = head;

            // 最终将tmp复制给head
            head = tmp;
        }

        return result;
    }

    /**
     * 203
     * 地址： https://leetcode-cn.com/problems/remove-linked-list-elements/
     * 移除链表元素(移除给定链表元素的值)
     */
    public ListNode removeElements(ListNode head, int val) {
        // 定义一个哑变量避免head被删除
        ListNode dump = new ListNode(0);
        dump.next = head;
        // pointer：标记遍历的位置
        ListNode input = dump;
        while (input.next != null) {
            if (input.next.val == val) {
                // 如果当前存在一个待移除的值，那么移除之后指针不需要往后移动
                input.next = input.next.next;
            } else {
                // 只有在不存在待移除的值，才需要将指针向下移动
                input = input.next;
            }
        }
        return dump.next;
    }

    /**
     * 328
     * 地址： https://leetcode-cn.com/problems/odd-even-linked-list/
     * 将奇数节点和偶数节点分开排列
     */
    public ListNode oddEvenList(ListNode head) {
        if (head == null || head.next == null) return head;

        ListNode oddHead = head;// 奇数链表的头，也是最终返回的结果
        ListNode oddTail = oddHead;// 奇数链表的尾，用来和偶数头对接(未遍历完是可作为迭代器)
        ListNode evenHead = head.next;// 偶数链表的头
        ListNode evenTail = evenHead;// 偶数链表的尾

        while (evenTail != null && evenTail.next != null) {
            /**  链表的问题：
             * 1.指针的变化会影响遍历条件的改变，这里需要注意；
             * 2.解决链表问题最好的办法是在脑中或者纸上把链表画出来；
             * ------------------------------------
             * 			oddHead
             * 			oddTail
             * 	round0		1	2	3	4	5
             * 				evenHead
             * 				evenTail
             * 				2	3	4	5
             *------------------------------------
             * 			oddHead
             * 			oddTail	    oddTail
             * 	round1		1		3	4	5
             *
             * 			evenHead
             * 			        		evenTail
             * 				2		4	    5
             *------------------------------------
             * 			oddHead
             * 							oddTail
             * 	round2		1		3		5
             * 			evenHead
             * 								evenTail
             * 				2		4		null
             */
            oddTail.next = evenTail.next;
            oddTail = oddTail.next;

            evenTail.next = oddTail.next;
            evenTail = evenTail.next;
        }
        // 循环结束将偶数链表拼接到奇数链表尾部
        oddTail.next = evenHead;
        return oddHead;
    }

    /**
     * 234
     * 地址： https://leetcode-cn.com/problems/palindrome-linked-list/
     * 判断一个链表是否为回文链表
     * 空间o(1)，时间o(n)
     */
    public boolean isPalindrome(ListNode head) {
        // 思路1：双指针，头尾和尾部一起向中间移动并判断是否相同
        // 思路2：将链表后半段翻转，然后依次遍历判断是否相同，(可满足空间o(1))
        // 实际生产中没必要过于追求空间复杂度，这里采用双指针的方式
        java.util.List<Integer> raw = new java.util.ArrayList<Integer>();
        // 将链表转换为数组
        while (head != null) {
            raw.add(head.val);
            head = head.next;
        }
        // 定义双指针
        int start = 0;
        int end = raw.size() - 1;
        while (start < end) {
            // 如果出现不相同即可退出
            if (!raw.get(start).equals(raw.get(end))) return false;

            start++;
            end--;
        }
        return true;
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
