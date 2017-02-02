package org.hammerlab.spark.test.suite

import java.util.Date

import org.apache.spark.SparkConf

/**
 * Base for tests that initialize [[SparkConf]]s (and [[org.apache.spark.SparkContext]]s, though that is left to
 * subclasses).
 */
trait SparkSuiteBase {
  def conf: SparkConf

  def numCores: Int = 4

  val uuid = s"${new Date()}-${math.floor(math.random * 10E4).toLong}"
  def appID: String

  def initConf(): SparkConf =
    conf
      // Set this explicitly so that we get deterministic behavior across test-machines with varying numbers of cores.
      .setMaster(s"local[$numCores]")
      .set("spark.app.name", this.getClass.getName)
      .set("spark.app.id", appID)
      .set("spark.driver.host", "localhost")
      .set("spark.ui.enabled", "false")
}
