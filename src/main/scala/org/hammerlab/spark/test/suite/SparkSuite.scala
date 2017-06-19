package org.hammerlab.spark.test.suite

import org.apache.hadoop.conf.Configuration
import org.apache.spark.SparkContext
import org.hammerlab.test.Suite

/**
 * Base for test suites that shar one [[org.apache.spark.SparkContext]] across all test cases.
 */
trait SparkSuite
  extends Suite
    with SparkSuiteBase {

  protected implicit var sc: SparkContext = _
  protected implicit var hadoopConf: Configuration = _

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    sc = makeSparkContext
    hadoopConf = sc.hadoopConfiguration
  }

  override def afterAll(): Unit = {
    super.afterAll()
    sc.stop()
    clearContext()
    sc = null
    hadoopConf = null
  }
}
