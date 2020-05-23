package cn.lang.spark_custom

import org.apache.spark.util.AccumulatorV2

/**
 * @author ：jianglang
 * @date ：Created in 2020/3/16 9:15 AM
 *       add the string into accumulator
 */
class Accumulator extends AccumulatorV2[String, java.util.Set[String]] {
  private val _collectSet = new java.util.HashSet[String]()

  override def isZero: Boolean = _collectSet.isEmpty

  // copy
  override def copy(): AccumulatorV2[String, java.util.Set[String]] = {
    val newAcc = new Accumulator()
    newAcc.synchronized {
      newAcc._collectSet.addAll(_collectSet)
    }
    newAcc
  }

  // reset
  override def reset(): Unit = _collectSet.clear()

  // add the element into accumulator in single node
  override def add(v: String): Unit = _collectSet.add(v)

  // merge accumulator from different nodes
  override def merge(other: AccumulatorV2[String, java.util.Set[String]]): Unit = {
    other match {
      case o: Accumulator => _collectSet.addAll(o._collectSet)
      case _ => throw new Exception("missed type")
    }
  }

  // result returnd to driver
  override def value: java.util.Set[String] = _collectSet
}
