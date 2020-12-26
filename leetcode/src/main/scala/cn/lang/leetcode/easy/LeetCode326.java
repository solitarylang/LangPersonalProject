package cn.lang.leetcode.easy;

/**
 * @author ：jianglang
 * @date ：Created in 2020/5/28 5:14 PM
 * @description ：给定一个整数，写一个函数判断是否3的幂次方
 * @version:
 */
public class LeetCode326 {
    public boolean isPowerOfThree(int n) {
        if (n < 1) {
            return false;
        }

        while (n % 3 == 0) {
            n /= 3;
        }

        return n == 1;
    }

    public static void main(String[] args) {

    }
}
