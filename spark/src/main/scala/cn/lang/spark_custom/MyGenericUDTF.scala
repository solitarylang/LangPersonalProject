package cn.lang.spark_custom

import java.util

import org.apache.hadoop.hive.ql.exec.{UDFArgumentException, UDFArgumentLengthException}
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory
import org.apache.hadoop.hive.serde2.objectinspector.{ObjectInspector, ObjectInspectorFactory, StructObjectInspector}

/**
 * @author ：jianglang
 * @date ：Created in 2019/3/11 7:45 PM
 * @description ：template for UDTF
 */
class MyGenericUDTF extends GenericUDTF {
  override def initialize(args: Array[ObjectInspector]): StructObjectInspector = {
    // check arguments
    if (args.length != 1) {
      throw new UDFArgumentLengthException("GenericUDTF takes only one argument")
    }
    if (args(0).getCategory != ObjectInspector.Category.PRIMITIVE) {
      throw new UDFArgumentException("GenericUDTF takes string as a parameter")
    }
    // the output columnsD
    val fieldNames = new util.ArrayList[String]
    val fieldOIs = new util.ArrayList[ObjectInspector]

    // new column name
    fieldNames.add("column")
    // new column type
    fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector)
    // inspect
    ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs)
  }

  /** run while one row come */
  override def process(objects: Array[AnyRef]): Unit = {
    val results = objects(0).toString.split(",")
    for (result <- results) {
      val outputs = new Array[String](1)
      outputs(0) = result
      forward(outputs)
    }
  }

  override def close(): Unit = {}
}
