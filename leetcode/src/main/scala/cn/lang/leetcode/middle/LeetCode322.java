package cn.lang.leetcode.middle;

/**
 * @Author ：lang
 * @Date ：Created in 2020/6/3 5:40 PM
 * @Description ：凑齐给定总金额最少需要的硬币个数
 */
public class LeetCode322 {
    // 采用动态规划的思路进行解决
    // 1.暴力法求解，2.暴力法复杂度计算以及优化(减少重复计算，比如有数据结构来缓存)
    public int coinChange(int[] coins, int amount) {
        /*
         * 题：只规定了面额，没有限定各面额的数量
         * 动态规划需要规划好当前问题和子问题之间的关系
         * 对于1块的硬币，那么子问题就是coinChange(int[] coins,int amount-1)的最优解+1
         * 同理需要查看其它币种的子问题故得暴力解法
         */
        if (amount <= 0) return 0;
        return coinChange(coins, amount, new int[amount]);
    }

    // 将保存的中间结果在递归方法中进行传递
    private int coinChange(int[] coins, int amount, int[] results) {
        // base case
        if (amount < 0) return -1;
        if (amount == 0) return 0;

        //
        if (results[amount - 1] != 0) return results[amount - 1];

        // 对所有币种进行递归校验得到最小值
        int min = Integer.MAX_VALUE;
        for (int coin : coins) {
            int tmp = coinChange(coins, amount - coin, results);
            if (tmp >= 0 && tmp < min) min = 1 + tmp;
        }
        results[amount - 1] = min == Integer.MAX_VALUE ? -1 : min;

        return results[amount-1];
    }

    public static void main(String[] args) {
        int[] coins1 = new int[]{1, 2, 5};
        int amount1 = 11;
        System.out.println(new LeetCode322().coinChange(coins1, amount1));

        int[] coins2 = new int[]{2};
        int amount2 = 3;
        System.out.println(new LeetCode322().coinChange(coins2, amount2));
    }
}
