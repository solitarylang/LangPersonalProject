package cn.lang.spark_mllib

import org.apache.spark.mllib.classification.{LogisticRegressionModel, LogisticRegressionWithLBFGS}
import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.mllib.tree.configuration.{Algo, Strategy}
import org.apache.spark.mllib.tree.impurity.Entropy
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 决策树使用的测试代码
 * target:
 * 1. 大致流程及各参数的设置方法和代表含义
 * 2. model保存的形式
 *
 * @author lang
 * @since 6/16/21 5:27 PM
 */
object DecisionTreeTest {
  def main(args: Array[String]): Unit = {
    // env
    val sparkConf = new SparkConf().setAppName("DecisionTreeTest").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)
    // source
    val raw: RDD[LabeledPoint] = MLUtils.loadLibSVMFile(sc, "/Users/langjiang/Ideaproject" +
      "/Github/LangPersonalProject/spark/src/main/resources/cn/lang/spark_mllib/iris.svm")
    val splits = raw.randomSplit(Array(0.8, 0.2))
    val training: RDD[LabeledPoint] = splits(0).cache()
    val test: RDD[LabeledPoint] = splits(1)

    // 训练模型
    val strategy = new Strategy(
      Algo.Classification,
      Entropy,
      10,
      2)

    val model = DecisionTree.train(training, strategy)

    val testResult: RDD[(Double, Double)] = test.map(line => {
      val predict = model.predict(line.features)

      println("predict:" + predict + ",line.label:" + line.label)

      (predict, line.label)
    })

    val metrics = new MulticlassMetrics(testResult)

    println(metrics.precision)
  }
}
