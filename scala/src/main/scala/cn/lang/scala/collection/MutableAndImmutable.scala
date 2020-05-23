package cn.lang.scala.collection

/**
 * @author ：jianglang
 * @date ：Created in 2020/5/14 11:46 AM
 * @description ：scala中mutable集合和immutable集合值区间的区别是什么
 * @version ：$version$
 */
class MutableAndImmutable {

  def isModify(input: scala.collection.Iterable[Int]): Unit = {
    println("第一次进入isModify方法： ")
    // 先打印传入集合的地址
    println("input修改前的地址: " + System.identityHashCode(input))
    // 打印其中的元素
    input.foreach(i => print(i + " "))
    println("")

    Thread.sleep(5000) // 线程睡5s

    println("睡眠后也就是试行增删后的结果:  ")
    // 先打印传入集合的地址
    println("input修改后的地址: " + System.identityHashCode(input))
    // 打印其中的元素
    input.foreach(i => print(i + " "))
    println("")

  }
}

object MutableAndImmutable {
  def main(args: Array[String]): Unit = {
    val mutable = scala.collection.mutable.Set(1, 2, 3)
    val immutable = scala.collection.immutable.Set(1, 2, 3)

    val bean = new MutableAndImmutable

    new Thread {
      override def run(): Unit = {
        Thread.sleep(2000)
        mutable.add(4)
      }
    }.start()

    bean.isModify(mutable)

    new Thread {
      override def run(): Unit = {
        Thread.sleep(2000)
        // 不可变集合对元素增删等操作是通过生成新集合来实现的
        val result = immutable.+(4)
      }
    }.start()

    bean.isModify(immutable)

    /** 打印结果
     * 第一次进入isModify方法：
     * input修改前的地址: 1032616650
     * 1 2 3
     * 睡眠后也就是试行增删后的结果:
     * input修改后的地址: 1032616650
     * 1 2 3 4
     * 第一次进入isModify方法：
     * input修改前的地址: 1798286609
     * 1 2 3
     * 睡眠后也就是试行增删后的结果:
     * input修改后的地址: 1798286609
     * 1 2 3
     */
  }
}