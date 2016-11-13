package org.hammerlab.magic.test.serde

import org.hammerlab.magic.test.version.Util
import org.hammerlab.magic.test.serde.JavaSerialization._
import org.hammerlab.magic.test.serde.util.{Foo, FooRegistrarTest, HasKryoSuite}

class SerializationSizeTest
  extends FooRegistrarTest
    with HasKryoSuite {

  val l = List("a" * 8, "b" * 8, "c" * 8)

  def checkJavaRoundTrip(o: Object, size: Int): Unit = {
    val bytes = javaBytes(o)
    bytes.length should be(size)
    javaRead[List[String]](bytes) should be(o)
  }

  test("java list") {
    checkJavaRoundTrip(
      l,
      // This List[String] gets compressed differently in Scala 2.10 vs. 2.11!
      if (Util.is2_10)
        263
      else
        166
    )
  }

  test("kryo list") {
    checkKryoRoundTrip(l, 32)
  }

  test("java foo") {
    checkJavaRoundTrip(Foo(187, "d" * 8), 104)
  }

  test("kryo foo") {
    checkKryoRoundTrip(Foo(187, "d" * 8), 12)
  }

  test("kryo foo class") {
    checkKryoRoundTrip(Foo(127, "d" *  8), 13, includeClass = true)
    checkKryoRoundTrip(Foo(128, "d" *  8), 13, includeClass = true)
    checkKryoRoundTrip(Foo(129, "d" * 16), 21, includeClass = true)
    checkKryoRoundTrip(Foo(130, "d" * 16), 21, includeClass = true)
    checkKryoRoundTrip(Foo(187, "d" *  8), 13, includeClass = true)
  }

  test("kryo 1 string") {
    checkKryoRoundTrip("a" * 8, 9)
  }

  test("kryo class and 1 string") {
    checkKryoRoundTrip("a" * 8, 10, includeClass = true)
  }

  test("kryo strings") {
    checkKryoRoundTrip(18, "a" * 8, "b" * 8)
  }

  test("kryo class and strings") {
    checkKryoRoundTrip(20, includeClass = true, "a" * 8, "b" * 8)
  }
}

