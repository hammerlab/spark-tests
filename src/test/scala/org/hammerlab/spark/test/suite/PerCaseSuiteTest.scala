package org.hammerlab.spark.test.suite

import org.apache.spark.scheduler.test.NumJobsUtil

class PerCaseSuiteTest
  extends PerCaseSuite
    with NumJobsUtil {

  // verify that each test case's first RDD has ID 0
  test("first context, num jobs") {
    numJobs should be(0)

    val rdd = sc.parallelize(1 to 10)

    rdd.id should be(0)

    numJobs should be(0)

    rdd.count should be(10)

    numJobs should be(1)
  }

  test("second context") {
    // new test case resets RDD-ID counter
    sc.parallelize(1 to 10).id should be(0)
  }

  test("making second context throws") {
    intercept[SparkContextAlreadyInitialized.type] { makeSparkContext }
  }

  test("sparkConf after context creation throws") {
    intercept[SparkConfigAfterInitialization] { sparkConf("aaa" → "bbb") }
  }
}
