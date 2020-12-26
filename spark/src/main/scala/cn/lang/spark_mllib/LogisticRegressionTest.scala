package cn.lang.spark_mllib

import org.apache.spark.mllib.classification.{LogisticRegressionModel, LogisticRegressionWithLBFGS}
import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author ：jianglang
 * @date ：Created in 2020/5/28 4:52 PM
 * @description ：logistic regression
 * @version ：$version$
 */
object LogisticRegressionTest {
  def main(args: Array[String]): Unit = {
    // env
    val sparkConf = new SparkConf().setAppName("SNMWithSGD").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)
    // source
    val raw: RDD[LabeledPoint] = MLUtils.loadLibSVMFile(sc, "/Users/langjiang/Ideaproject" +
      "/Github/LangPersonalProject/spark/src/main/resources/cn/lang/spark_mllib/iris.svm")
    val splits = raw.randomSplit(Array(0.6, 0.4))
    val training: RDD[LabeledPoint] = splits(0).cache()
    val test: RDD[LabeledPoint] = splits(1)

    // 训练模型
    val model: LogisticRegressionModel = new LogisticRegressionWithLBFGS().setNumClasses(3).run(training)
    val testResult: RDD[(Double, Double)] = test.map(line => {
      (model.predict(line.features), line.label)
    })

    val metrics = new MulticlassMetrics(testResult)

    println(metrics.precision)
    println(model.intercept)
    println(model.weights)
  }
}
