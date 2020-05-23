package cn.lang.spark_custom

import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, LongType, StructField, StructType}

/**
 * @author ：jianglang
 * @date ：Created in 2020/3/10 11:36 PM
 * @description ：template for UDAF : calculate average
 */
class MyUserDefinedAggregateFunction extends UserDefinedAggregateFunction {

  /** this is input schema and need a List of StructField */
  override def inputSchema: StructType = new StructType()
    .add("doubleInput", DoubleType)
    .add("longInput", LongType) // this example of StructType

  /** the temporary result */
  override def bufferSchema: StructType = StructType(
    StructField("doubleBuffer", DoubleType)
      :: StructField("longBuffer", LongType)
      :: Nil) // the same with inputSchema

  /** dataType that returned */
  override def dataType: DataType = DoubleType

  /** check deterministic or not */
  override def deterministic: Boolean = true

  /** the initialization of buffer */
  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    // match the bufferSchema
    buffer(0) = 0D
    buffer(1) = 0L
  }

  /** calculate in single node */
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    // update the result to buffer1
    buffer(0) = buffer.getDouble(0) + input.getDouble(0)
    buffer(1) = buffer.getLong(1) + input.getLong(1)
  }

  /** merge the result over different nodes */
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    // update the result to buffer1
    buffer1.update(0, buffer1.getDouble(0) + buffer2.getDouble(0))
    buffer1.update(1, buffer1.getLong(1) + buffer2.getLong(1))
  }

  /** output */
  override def evaluate(buffer: Row): Any = buffer.getDouble(0) / buffer.getLong(1)
}
