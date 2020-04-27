package cn.lang.scala_exercise.interview

import org.scalatest.{BeforeAndAfter, FunSuite}

/**
 * @author ：jianglang
 * @date ：Created in 2020/4/28 12:09 AM
 * @description ：SortHandy Test
 * @version ：1.0.0
 */
class SortHandyTest extends FunSuite with BeforeAndAfter {

  private var ints: Array[Int] = _
  private var left: List[Int] = _
  private var right: List[Int] = _
  private var sortHandy: SortHandy = _

  before {
    sortHandy = new SortHandy
    ints = List(11, 6, 21, 5, 31, 4).toArray
    left = List(1, 3, 5)
    right = List(2, 4, 6)
  }

  test("quickSort") {
    val result = sortHandy.quickSort(ints.toList)
    assert(result.mkString("").equals("456112131"))
  }

  test("bubbleSort") {
    val result = sortHandy.bubbleSort(ints)
    assert(result.mkString("").equals("456112131"))
  }

  test("mergeSort") {
    val result = sortHandy.mergeSort(left, right)
    assert(result.mkString("").equals("123456"))
  }

  test("binarySearch") {
    val reuslt = sortHandy.quickSort(ints.toList)
    val index1 = sortHandy.binarySearch(reuslt.toArray, 0, 5, 21)
    assert(index1 == 4)
    val index2 = sortHandy.binarySearch(reuslt.toArray, 0, 5, 5)
    assert(index2 == 1)
    try {
      val index3 = sortHandy.binarySearch(reuslt.toArray, 0, 5, 100)
    }
    catch {
      case e: Exception => assert(e.getMessage.equals("requirement failed: binary search arguments illegal"))
    }
  }
}
