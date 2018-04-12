package org.hammerlab.spark.test.suite

import hammerlab.test.Suite

/**
 * Base for test suites that shar one [[org.apache.spark.SparkContext]] across all test cases.
 */
abstract class SparkSuite
  extends Suite
    with SparkSuiteBase {

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    makeSparkContext
  }

  override def afterAll(): Unit = {
    super.afterAll()
    clear()
  }
}
