package cn.lang.spark_mllib

import org.apache.spark.mllib.classification.{SVMModel, SVMWithSGD}
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.rdd.RDD

/**
 * @author ：jianglang
 * @date ：Created in 2020/5/26 11:50 AM
 * @description ：使用SVM分离经典Iris数据集
 */
object SVMWithSGDTest {
  def main(args: Array[String]): Unit = {
    // env
    val sparkConf = new SparkConf().setAppName("SNMWithSGD").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    // 使用awk工具将Iris数据集转换成LIBSVM格式
    // before: 5.1,3.5,1.4,0.2,Iris-setosa
    // later: 0 1:5.1 2:3.5 3:1.4 4:0.2
    // 因为使用向量机所以构造超平面来区分是否是`setosa`花(给label=0,其他label=1)
    //    awk脚本=awk -F ','
    //    '/setosa/ {print "0 1:"$1" 2:"$2" 3:"$3" 4:"$4};
    //    /versicolor/ {print "1 1:"$1" 2:"$2" 3:"$3" 4:"$4};
    //    /virginica/ {print "1 1:"$1" 2:"$2" 3:"$3" 4:"$4};'
    //    iris.data > iris.svm

    // source: LabeledPoint=label+features
    val raw: RDD[LabeledPoint] = MLUtils.loadLibSVMFile(sc, "/Users/langjiang/Ideaproject" +
      "/Github/LangPersonalProject/spark/src/main/resources/cn/lang/spark_mllib/iris.svm")
    // e.g. (1.0,(4,[0,1,2,3],[6.6,3.0,4.4,1.4]))

    // 将数据集按照比例分为训练集和测试机，(随机数种子确定，那么数据集划分执行多少次也都是一样的)
    val splits = raw.randomSplit(Array(0.6, 0.4), 123L)

    val training: RDD[LabeledPoint] = splits(0).cache()

    val test: RDD[LabeledPoint] = splits(1)
    // @param numIterations 梯度下降的迭代次数
    // @param stepSize 每次迭代梯度下降的大小，也就是学习率/学习步长，默认1.0，过大可能导致来回震荡，过小可能下降过慢或到局部洼地
    // @param regParam 正则化参数，用于防止模型过拟合现象，默认是0.01
    // @param miniBatchFraction 每一次迭代用于计算的数据比例，默认1.0即100%
    val model: SVMModel = SVMWithSGD.train(training, 100)

    // 模型最终是划分label为0或者1，使用此方法可以获取到模型计算的原始分数
    // e.g. 例如模型计算得分是3.0，那么模型给的分类就是1.0，但是clear之后预测结果就是计算得分而不是最后分类结果
    model.clearThreshold()

    val testResult: RDD[(Double, Double)] = test.map(line => {
      (model.predict(line.features), line.label)
    })
    // (2.125031770237907,1.0)前面是计算得分，后面是实际标签

    // 评估计算，模型预测和实际标签符合的正确率
    // numBins： ？？？
    val metrics: BinaryClassificationMetrics = new BinaryClassificationMetrics(testResult, numBins = 0)
    // 由于预测百分百准确，可知混淆矩阵即metrics中confusions
    /*          positive  negative
    * positive   TP=37      FN=0    P=37
    * negative   FP=0       TN=23   N=23
    */
    println(model.intercept)
    println(model.weights)

    // 计算roc曲线下面的面积，即AUC：area under roc curve面积越大模型拟合越好，正常来说是[0.5,1.0]区间
    val roc: Double = metrics.areaUnderROC() // 1.0
    // 计算pr曲线下面的面积(精准率-召回率)
    val pr: Double = metrics.areaUnderPR() // 1.0
  }
}
