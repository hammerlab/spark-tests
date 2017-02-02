package org.hammerlab.spark.test.suite

import org.apache.spark.{ SparkConf, SparkContext }
import org.hammerlab.test.Suite

/**
 * Base for test-suites that expose a fresh [[SparkContext]] for each test-case.
 */
trait PerCaseSuite
  extends Suite
    with SparkSuiteBase {

  implicit var sc: SparkContext = _

  var conf: SparkConf = _

  override val appID = s"${this.getClass.getSimpleName}-$uuid"

  protected def setConfigs(conf: SparkConf): Unit = {}

  before {
    conf = new SparkConf()

    initConf()

    // Opportunity for subclasses to set/override configs.
    setConfigs(conf)

    sc = new SparkContext(conf)
  }

  after {
    sc.stop()

    // To avoid Akka rebinding to the same port, since it doesn't unbind immediately on shutdown
    System.clearProperty("spark.driver.port")

    sc = null
    conf = null
  }
}
