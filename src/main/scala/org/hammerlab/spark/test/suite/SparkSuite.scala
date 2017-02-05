package org.hammerlab.spark.test.suite

import com.holdenkarau.spark.testing.SharedSparkContext
import org.hammerlab.test.Suite

/**
 * Base for test suites that use a [[org.apache.spark.SparkContext]].
 *
 * Thin wrapper over [[SharedSparkContext]], additionally initializing a checkpoints directory and tweaking some default
 * [[org.apache.spark.SparkConf]] values.
 */
trait SparkSuite
  extends Suite
    with SharedSparkContext
    with SparkSuiteBase {

  // Expose the SparkContext as an implicit.
  implicit lazy val sparkContext = sc

  initConf()

  conf.set("spark.driver.allowMultipleContexts", "true")

  // Set checkpoints dir so that tests that use RDD.checkpoint don't fail.
  override def beforeAll(): Unit = {
    super.beforeAll()
    val checkpointsDir = tmpDir()
    sc.setCheckpointDir(checkpointsDir.toString)
  }
}
