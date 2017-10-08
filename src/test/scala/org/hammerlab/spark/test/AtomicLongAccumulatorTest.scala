package org.hammerlab.spark.test

import org.hammerlab.spark.test.suite.SparkSuite

class AtomicLongAccumulatorTest
  extends SparkSuite {

  test("instance") {
    val acc = AtomicLongAccumulator()
    sc.register(acc, "acc")
    sc
    .parallelize(
      1 to 10,
      numSlices = 4
    )
    .map {
      n ⇒
        acc.add(n)
        n.toString
    }
    .collect should be(1 to 10 map(_.toString))

    acc.value should be(55)
  }

  test("static") {
    import AtomicLongAccumulatorTest.acc
    sc.register(acc, "acc")
    sc
      .parallelize(
        1 to 10,
        numSlices = 4
      )
      .map {
        n ⇒
          acc.add(n)
          n.toString
      }
      .collect should be(1 to 10 map(_.toString))

    acc.value should be(55)
  }
}

object AtomicLongAccumulatorTest {
  val acc = AtomicLongAccumulator()
}
