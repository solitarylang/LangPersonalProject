package cn.lang.leetcode.easy;


import java.util.HashMap;
import java.util.Map;

/**
 * @author ：jianglang
 * @date ：Created in 2020/5/26 1:08 PM
 * @description：两数之和
 * @version: $
 */
public class LeetCode1 {
    public int[] twoSum(int[] nums, int target) {
        // 创建hash表存储
        Map<Integer, Integer> result = new HashMap();

        for (int i = 0; i < nums.length; i++) {
            if (result.containsKey(target - nums[i])) {
                return new int[]{result.get(target - nums[i]), i};
            }
            result.put(nums[i], i);
        }

        throw new IllegalArgumentException("No result");
    }

    public static void main(String[] args) {

    }
}
