package org.hammerlab.spark.test.suite

import com.esotericsoftware.kryo.io.{ Input, Output }
import com.esotericsoftware.kryo.{ Kryo, Serializer }
import org.apache.spark.SparkException

import scala.collection.mutable

class KryoSparkSuiteTest
  extends KryoSparkSuite {

  register(classOf[mutable.WrappedArray.ofRef[_]])
  register(classOf[Foo] → new FooSerializer)
  register(classOf[Array[Foo]])

  test("custom serializer is used") {
    intercept[SparkException] {
      sc.parallelize(Seq(Foo(1, "a"), Foo(2, "b"))).count()
    }.getMessage should include("FooException")
  }
}

case class Foo(n: Int, s: String)

case class FooException() extends Exception

class FooSerializer extends Serializer[Foo] {
  override def write(kryo: Kryo, output: Output, `object`: Foo): Unit = throw FooException()
  override def read(kryo: Kryo, input: Input, `type`: Class[Foo]): Foo = ???
}
