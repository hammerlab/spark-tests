package org.hammerlab.spark.test.suite

import java.util.Date

import org.apache.spark.SparkContext
import org.hammerlab.test.Suite

/**
 * Base for test-suites that expose a fresh [[SparkContext]] for each test-case.
 */
trait PerCaseSuite
  extends Suite
    with SparkSuiteBase {

  protected implicit var sc: SparkContext = _

  val uuid = s"${new Date()}-${math.floor(math.random * 1E5).toInt}"

  val appID = s"${this.getClass.getSimpleName}-$uuid"

  before {
    sc = makeSparkContext
  }

  after {
    sc.stop()
    clearContext()

    // To avoid Akka rebinding to the same port, since it doesn't unbind immediately on shutdown
    System.clearProperty("spark.driver.port")

    sc = null
  }
}
