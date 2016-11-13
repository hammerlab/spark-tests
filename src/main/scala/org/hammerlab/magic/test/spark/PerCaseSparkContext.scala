package org.hammerlab.magic.test.spark

import java.util.Date

import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfterEach, Suite}

trait PerCaseSparkContext
  extends BeforeAndAfterEach {
  self: Suite =>

  implicit var sc: SparkContext = _

  val uuid = new Date().toString + math.floor(math.random * 10E4).toLong.toString

  val appID = s"${this.getClass.getSimpleName}-$uuid"

  private var conf: SparkConf = _

  protected def setConfigs(conf: SparkConf): Unit = {}

  override def beforeEach() {
    super.beforeEach()

    conf =
      new SparkConf()
        .setMaster("local[4]")
        .setAppName(this.getClass.getSimpleName)
        .set("spark.ui.enabled", "false")
        .set("spark.app.id", appID)

    // Opportunity for subclasses to set/override configs.
    setConfigs(conf)

    sc = new SparkContext(conf)
  }

  override def afterEach() {
    super.afterEach()
    sc.stop()

    // To avoid Akka rebinding to the same port, since it doesn't unbind immediately on shutdown
    System.clearProperty("spark.driver.port")

    sc = null
  }
}
