package cn.lang.spark_mllib

import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
import org.apache.spark.ml.{Pipeline, PipelineStage}
import org.apache.spark.ml.feature.{OneHotEncoder, StringIndexer, VectorAssembler}
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ListBuffer

/**
 * @author ：jianglang
 * @date ：Created in 2020/5/15 10:57 PM
 * @description ：one-hot编码实践
 * @version ：1.0.0
 */
object OneHotEncoding {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().master("local[*]").getOrCreate()

    val data = spark.read.json("/Users/langjiang/Ideaproject/Github/LangPersonalProject/spark/src/main/resources/cn/lang/spark_mllib/lr_test03.json")

    /** 要进行OneHotEncoder编码的字段 */
    val categoricalColumns = Array("gender", "children")
    /** 采用Pileline方式处理机器学习流程 */
    val stagesArray = new ListBuffer[PipelineStage]()
    for (cate <- categoricalColumns) {
      /** 使用StringIndexer 建立类别索引 */
      val indexer = new StringIndexer().setInputCol(cate).setOutputCol(s"${cate}Index")
      /** 使用OneHotEncoder将分类变量转换为二进制稀疏向量 */
      val encoder = new OneHotEncoder().setInputCol(indexer.getOutputCol).setOutputCol(s"${cate}classVec")
      stagesArray.append(indexer, encoder)
    }

    val numericCols = Array("affairs", "age", "yearsmarried", "religiousness", "education", "occupation", "rating")
    val assemblerInputs = categoricalColumns.map(_ + "classVec") ++ numericCols
    /** 使用VectorAssembler将所有特征转换为一个向量 */
    val assembler = new VectorAssembler().setInputCols(assemblerInputs).setOutputCol("features")
    stagesArray.append(assembler)

    val pipeline = new Pipeline()
    pipeline.setStages(stagesArray.toArray)
    /** fit() 根据需要计算特征统计信息 */
    val pipelineModel = pipeline.fit(data)
    /** transform() 真实转换特征 */
    val dataset = pipelineModel.transform(data)
    dataset.show(false)

    import spark.implicits._
    /** 随机分割测试集和训练集数据，指定seed可以固定数据分配 */
    val Array(trainingDF, testDF) = dataset.randomSplit(Array(0.6, 0.4), seed = 12345)
    println(s"trainingDF size=${trainingDF.count()},testDF size=${testDF.count()}")
    val lrModel = new LogisticRegression().setLabelCol("affairs").setFeaturesCol("features").fit(trainingDF)
    val predictions = lrModel.transform(testDF).select($"affairs".as("label"), $"features", $"rawPrediction", $"probability", $"prediction")
    predictions.show(false)
    /** 使用BinaryClassificationEvaluator来评价我们的模型。在metricName参数中设置度量。 */
    val evaluator = new BinaryClassificationEvaluator()
    evaluator.setMetricName("areaUnderROC")
    val auc = evaluator.evaluate(predictions)
    println(s"areaUnderROC=$auc")
  }
}