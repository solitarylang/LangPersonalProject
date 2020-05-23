package cn.lang.leetcode.easy;

import java.util.Arrays;

/**
 * @author ：jianglang
 * @date ：Created in 2020/5/23 2:58 PM
 * @description ：数组表示的整数加一
 */
public class LeetCode66 {

    public int[] plusOne(int[] digits) {
        // 很明显，数组的每一个元素都是0-9
        // 很直接的方法就是对数组倒序遍历，将加一的结果保存在另外一个数组中，最终返回
        // 1.只有在数组中元素都是9的时候才会设计到数组长度的改变，这个在最后新建数组返回结果
        // 2.只有在上一位出现9的时候才有可能影响下一位的计算，如果没有即可终止循环

        int pointer = 1;// 用于记录是否需要进行下一位的计算
        // 倒序遍历
        for (int i = digits.length - 1; i >= 0; i--) {
            if (pointer == 1) {
                int tmp = pointer;
                pointer = (digits[i] + tmp) / 10;
                digits[i] = (digits[i] + tmp) % 10;
            } else {
                return digits;
            }
        }

        // 如果遍历完，pointer==1，那么就需要返回新数组，而且末尾肯定都是0
        if (pointer == 1) {
            int[] tmp = new int[digits.length + 1];
            for (int i = 0; i < tmp.length; i++) {
                if (i == 0) tmp[i] = pointer;
                else tmp[i] = 0;
            }
            return tmp;
        }
        return digits;
    }

    public static void main(String[] args) {
        // 对集中极端数据进行验证
        int[] input1 = {0};
        int[] input2 = {9,9,9};
        int[] input3 = {1,2,9,1,2};

        System.out.println(Arrays.toString(new LeetCode66().plusOne(input1)));
        System.out.println(Arrays.toString(new LeetCode66().plusOne(input2)));
        System.out.println(Arrays.toString(new LeetCode66().plusOne(input3)));
    }
}
