package org.hammerlab.magic.test.serde

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}

import org.hammerlab.magic.test.serde.util.Foo
import org.scalatest.{FunSuite, Matchers}

case class Bar(a: Int, @transient b: Int, @transient c: Foo) {
  lazy val d: Int = a * 2
  lazy val e: Int = b * 2
  lazy val f: Int = c.n * 2

  @transient lazy val g: Int = a * 2
  @transient lazy val h: Int = b * 2
  @transient lazy val i: Int = c.n * 2
}

/**
 * Test that verifies behavior under Java-serde of @transient and/or lazy vals.
 */
class JavaTransientSerializationTest
  extends FunSuite
    with Matchers {

  test("simple") {
    val baos = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(baos)

    val bar = Bar(2, 3, Foo(4, "a"))

    oos.writeObject(bar)
    oos.close()

    val bytes = baos.toByteArray
    val bais = new ByteArrayInputStream(bytes)
    val ois = new ObjectInputStream(bais)

    val bar2 = ois.readObject().asInstanceOf[Bar]

    bar2.a should be(2)
    bar2.b should be(0)
    bar2.c should be(null)

    bar2.d should be(4)
    bar2.e should be(0)

    intercept[NullPointerException] {
      bar2.f
    }

    bar2.g should be(4)
    bar2.h should be(0)

    intercept[NullPointerException] {
      bar2.i
    }
  }
}
