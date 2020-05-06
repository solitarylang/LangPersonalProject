package cn.lang.scala.function

/**
 * @author ：jianglang
 * @date ：Created in 2020/5/6 8:46 AM
 * @description ：高阶函数
 * @version ：1.0.0
 */
object HighStageFunction {

  def lowStageFunction(input: Int): Nothing = {
    println(s"this is lowStageFunction and input is ${input}")
    throw new Exception("exception in lowStageFunction")
  }

  /* inout is another function */
  def highStageFunction(op: Int => Unit): Unit = {
    /**
     * op代表传入的函数，:后面代表传入函数的输入，Unit代表传入函数的输出
     */
    println("this is highStageFunction")
    try {
      op(10)
    } catch {
      case e: Exception => println("highStageFunction catch: " + e.getMessage)
    }
  }

  def main(args: Array[String]): Unit = {
    highStageFunction(lowStageFunction)
  }
}
