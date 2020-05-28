package cn.lang.leetcode.easy;

/**
 * @author ：jianglang
 * @date ：Created in 2020/5/28 5:28 PM
 * @description ：统计所有小于非负整数n的质数的数量
 */
public class LeetCode224 {
    /**
     * 思路1
     * 1.外层遍历小于n的所有数，内层循环判断遍历数是否为质数
     * 思考1
     * 1.如何判断一个数是否是质数?
     */
    boolean isPrime(int n) {
        if (n <= 1) return false;
        // 起点需要慎重选择，n=2，3，4不会进循环
        // 导致n=4会有误判,可将数值较小的情况遍历返回
        if (n == 4) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public int countPrimes(int n) {
        int count = 0;
        // 做好边界点的判断条件
        if (n <= 2) return 0;
        for (int i = 2; i < n; i++) {
            if (isPrime(i)) count += 1;
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(new LeetCode224().isPrime(1));
        System.out.println(new LeetCode224().isPrime(2));
        System.out.println(new LeetCode224().isPrime(3));
        System.out.println(new LeetCode224().isPrime(4));
        System.out.println(new LeetCode224().isPrime(5));
        System.out.println(new LeetCode224().isPrime(6));
        System.out.println(new LeetCode224().isPrime(7));
        System.out.println(new LeetCode224().isPrime(8));
        System.out.println(new LeetCode224().isPrime(9));

        System.out.println(new LeetCode224().countPrimes(10));
    }
}
