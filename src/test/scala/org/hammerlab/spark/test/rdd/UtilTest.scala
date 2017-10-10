package org.hammerlab.spark.test.rdd

import org.hammerlab.spark.test.suite.SparkSuite

class UtilTest
  extends SparkSuite {

  import Util.makeRDD

  test("partitions") {
    val partitions =
      Seq(
        Nil,
        1 to 10,
        Nil,
        Seq(20),
        30 to 40,
        Nil
      )

    makeRDD(partitions: _*)
      .mapPartitions {
        it ⇒ Iterator(it.toArray)
      }
      .collect should be(partitions)
  }
}
