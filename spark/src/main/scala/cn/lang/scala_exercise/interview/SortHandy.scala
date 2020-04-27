package cn.lang.scala_exercise.interview

/**
 * @author ：jianglang
 * @date ：Created in 2020/4/28 12:06 AM
 * @description ：手写scala排序算法
 * @version ：1.0.0
 */

/** asc default */
class SortHandy {

  /* 快排： 将排序拆分两部分分开排序，递归思想 */
  def quickSort(list: List[Int]): List[Int] = list match {
    case Nil => Nil
    case List() => List()
    case head :: tail =>
      val (left, right) = tail.partition(_ < head)
      quickSort(left) ::: head :: quickSort(right)
  }

  /* 冒泡: 将局部最大转移到最后面 */
  def bubbleSort(input: Array[Int]): Array[Int] = {
    require(input != null && input.length != 0)
    var tmp: Int = 0
    for (i <- input.indices) {
      for (j <- 0 until input.length - i - 1) {
        if (input(j) > input(j + 1)) {
          tmp = input(j)
          input(j) = input(j + 1)
          input(j + 1) = tmp
        }
      }
    }
    input
  }

  /* 归并, 核心是zip式比较大小，可以理解为两个指针分别移动 */
  def mergeSort(left: List[Int], right: List[Int]): List[Int] = (left, right) match {
    case (Nil, right) => right
    case (left, Nil) => left
    case (x :: leftTail, y :: rightTail) =>
      if (x <= y) x :: mergeSort(leftTail, right)
      else y :: mergeSort(left, rightTail)
  }

  /* 二分查找, 如果有相同的值可以使用ArrayBuffer存储 */
  def binarySearch(input: Array[Int], start: Int, end: Int, target: Int): Int = {
    require(start <= end,"binary search arguments illegal")
    val mid = (start + end) / 2
    println(mid)
    if (input(mid) == target) return mid
    if (input(mid) < target) binarySearch(input, mid + 1, end, target)
    else binarySearch(input, start, mid - 1, target)
  }

}
