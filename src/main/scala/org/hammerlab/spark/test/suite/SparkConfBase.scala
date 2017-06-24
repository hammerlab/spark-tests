package org.hammerlab.spark.test.suite

import java.lang.System.setProperty

import org.apache.spark.SparkConf

import scala.collection.mutable

trait SparkConfBase {
  private val sparkConfs = mutable.Map[String, String]()

  protected def makeSparkConf: SparkConf = {
    val sparkConf = new SparkConf()
    for {
      (k, v) ← sparkConfs
    } {
      sparkConf.set(k, v)
    }
    sparkConf
  }

  protected def setSparkProps(): Unit =
    for {
      (k, v) ← sparkConfs
    } {
      setProperty(k, v)
    }

  protected def sparkConf(confs: (String, String)*): Unit =
    for {
      (k, v) ← confs
    } {
      sparkConfs(k) = v
    }

  protected def numCores: Int = 4

  sparkConf(
    // Set this explicitly so that we get deterministic behavior across test-machines with varying numbers of cores.
    "spark.master" → s"local[$numCores]",
    "spark.app.name" → this.getClass.getName,
    "spark.driver.host" → "localhost",
    "spark.ui.enabled" → "false"
  )
}
