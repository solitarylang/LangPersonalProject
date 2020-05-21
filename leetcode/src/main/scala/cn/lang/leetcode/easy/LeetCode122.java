package cn.lang.leetcode.easy;

/**
 * @author ：jianglang
 * @version : 1.0.0
 * @date ：Created in 2020/5/21 7:59 PM
 * @description ：买卖股票的最佳时机II https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-ii/
 */
public class LeetCode122 {

    public int maxProfit(int[] prices) {
        // 核心理念就是低价买入，高价卖出
        int result = 0;
        // 一个指针记录买入的位置
        int buy = -1; // 初始化
        // 一个指针记录当前遍历的位置
        for (int cur = 0; cur < prices.length - 1; cur++) {
            if (prices[cur] < prices[cur + 1] && buy == -1) {
                buy = cur;
            } else if (prices[cur] >= prices[cur + 1] && buy != -1) {
                result += prices[cur] - prices[buy];
                buy = -1;
            }
        }
        // 最后的情况可以通过哑结点的方式或者在循环中判断来处理
        if (buy != -1) result += prices[prices.length - 1] - prices[buy];

        return result;
    }

    public static void main(String[] args) {
        int[] prices = {7, 1, 5, 3, 6, 4};
        System.out.println(new LeetCode122().maxProfit(prices));
    }
}
