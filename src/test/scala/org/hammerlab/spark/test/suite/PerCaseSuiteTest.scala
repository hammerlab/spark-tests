package org.hammerlab.spark.test.suite

class PerCaseSuiteTest
  extends PerCaseSuite {

  // verify that each test case's first RDD has ID 0
  test("first context") {
    sc.parallelize(1 to 10).id should be(0)
  }

  test("second context") {
    sc.parallelize(1 to 10).id should be(0)
  }
}
