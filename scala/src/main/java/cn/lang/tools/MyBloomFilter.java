package cn.lang.tools;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

/**
 * @Author ：lang
 * @Date ：Created in 2020/6/3 8:21 AM
 * @Description ：布隆过滤器的使用
 */
public class MyBloomFilter {
    public static void main(String[] args) {
        // 1.根据预估数据量n和误判率Ffp估计比特数组的长度m
        // 2.根据预估数据量n和比特数组的长度m估计哈希函数的个数k，如果是小数，四舍五入取整
        // 3.根据计算结果创建布隆过滤器
        BloomFilter<String> bf = BloomFilter.create(
                Funnels.stringFunnel(Charsets.UTF_8), 10000, 0.01);


        // 将数据通过单点的方式添加，也可批量添加，比如布隆过滤器重构时
        for (int i = 0; i < 10000; i++) {
            // 添加一个元素可查看BloomFilterStrategies.put方法
            bf.put(i + "common log body");
        }

        // 统计一下确定存在的数据是否存在误判
        int wrongJudge = 0;
        for (int i = 0; i < 10000; i++) {
            if (!bf.mightContain(i + "common log body")) wrongJudge++;
        }
        System.out.println("全部都是确定存在，判断错误的个数: " + wrongJudge);

        // 统计一下确定不存在的数据的误判数量
        int wrongJudge1 = 0;
        for (int i = 10000; i < 20000; i++) {
            if (bf.mightContain(i + "common log body")) wrongJudge1++;
        }
        System.out.println("全部都是确定不存在，判断错误的个数: " + wrongJudge1);
    }
}
