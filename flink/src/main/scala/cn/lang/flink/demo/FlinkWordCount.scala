package cn.lang.flink.demo

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}

/**
 * @author ：jianglang
 * @date ：Created in 2019/5/2 11:07 AM
 * @description ：flink word count from local file
 * @version ：1.0.0
 */
object FlinkWordCount {

  def main(args: Array[String]): Unit = {
    // env
    val env = ExecutionEnvironment.getExecutionEnvironment

    val input = "/Users/langjiang/Ideaproject/Github/LangPersonalProject" +
      "/flink/src/main/resources/cn/lang/flink/demo/wordcount.txt"

    /** source */
    val raw_data: DataSet[String] = env.readTextFile(input) //从本地文件中读取数据，每一个iterm就是一行数据

    /** Exception: ------------------------------------------------------ **
     * flink could not find implicit value for evidence parameter of type **
     * 这是因为map方法中有[R: TypeInformation]需要定义，推荐直接导入的方式解决  **
     */
    import org.apache.flink.api.scala._
    /** transformation */
    val result: AggregateDataSet[(String, Int)] = raw_data
      .flatMap(_.split(" ")) // 对每行数据按照空格进行分割
      .map((_, 1)) // 对每一个单词进行计数
      .groupBy(0) // 按照索引位在0号的单词进行分组
      .sum(1) // 按照索引位在1号的单词数量进行加和
    /** sink */
    result.print()
  }
}
