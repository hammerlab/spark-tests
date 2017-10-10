package org.hammerlab.spark.test.suite

import com.esotericsoftware.kryo.io.{ Input, Output }
import com.esotericsoftware.kryo.{ Kryo, Serializer }
import org.apache.spark.SparkException
import org.hammerlab.kryo._

import scala.collection.mutable

class KryoSparkSuiteTest
  extends KryoSparkSuite
    with SparkSerialization {

  register(
    cls[mutable.WrappedArray.ofRef[_]],
    arr[Foo],
    cls[Bar]
  )

  test("spark job custom serializer") {
    intercept[SparkException] {
      sc.parallelize(Seq(Foo(1, "a"), Foo(2, "b"))).count()
    }
    .getMessage should include("FooException")
  }

  test("local custom serializer") {
    val bar1 = Bar(1, "a")
    deserialize[Bar](serialize(bar1)) should be(bar1)

    val bar2 = Bar(2, "b")
    deserialize[Bar](serialize(bar2): Array[Byte]) should be(bar2)
  }
}

case class Foo(n: Int, s: String)

object Foo {
  implicit val serializer: Serializer[Foo] =
    new Serializer[Foo] {
      override def write(kryo: Kryo, out: Output, foo: Foo): Unit = throw FooException()
      override def read(kryo: Kryo, in: Input, cls: Class[Foo]): Foo = ???
    }
}

case class FooException() extends Exception

case class Bar(n: Int, s: String)
