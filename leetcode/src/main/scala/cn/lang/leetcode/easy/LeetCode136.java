package cn.lang.leetcode.easy;

/**
 * @author ：jianglang
 * @date ：Created in 2020/5/22 12:59 PM
 * @description：找出只出现一次的数字 https://leetcode-cn.com/problems/single-number/
 * @version: 1.0.0$
 */
public class LeetCode136 {
    // 数组中所有的数字都出现了两次，只有一个数字仅出现一次，找出它
    public int singleNumber(int[] nums) {
        int result = nums[0];

        for (int i = 1; i < nums.length; i++) {
            result = nums[i] ^ result;
        }
        return result;
    }

    public static void main(String[] args) {
        int[] nums = {2, 2, 1};

        System.out.println(new LeetCode136().singleNumber(nums));

    }
}
