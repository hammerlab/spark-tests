package org.hammerlab.spark.test.suite

import org.apache.spark.{ SparkConf, SparkContext }
import org.hammerlab.test.Suite

import scala.collection.mutable

/**
 * Base for tests that initialize [[SparkConf]]s (and [[org.apache.spark.SparkContext]]s, though that is left to
 * subclasses).
 */
trait SparkSuiteBase
  extends Suite {

  private var _sc: SparkContext = _

  def makeSparkContext: SparkContext =
    Option(_sc) match {
      case Some(sc) ⇒
        throw SparkContextAlreadyInitialized
      case None ⇒
        val sparkConf = new SparkConf()
        for {
          (k, v) ← sparkConfs
        } {
          sparkConf.set(k, v)
        }

        _sc = new SparkContext(sparkConf)
        val checkpointsDir = tmpDir()
        _sc.setCheckpointDir(checkpointsDir.toString)

        _sc
    }

  def clearContext(): Unit = {
    Option(_sc) match {
      case Some(sc) ⇒
        _sc = null
      case None ⇒
        throw NoSparkContextToClear
    }
  }

  private val sparkConfs = mutable.Map[String, String]()

  def sparkConf(confs: (String, String)*): Unit =
    Option(_sc) match {
      case Some(sc) ⇒
        throw SparkConfigAfterInitialization(confs)
      case None ⇒
        for {
          (k, v) ← confs
        } {
          sparkConfs(k) = v
        }
    }

  def numCores: Int = 4

  sparkConf(
    // Set this explicitly so that we get deterministic behavior across test-machines with varying numbers of cores.
    "spark.master" → s"local[$numCores]",
    "spark.app.name" → this.getClass.getName,
    "spark.driver.host" → "localhost"
  )

}

case object SparkContextAlreadyInitialized extends IllegalStateException

case class SparkConfigAfterInitialization(confs: Seq[(String, String)])
  extends IllegalStateException(
    s"Attempting to configure SparkContext after initialization:\n" +
      (
        for {
          (k, v) ← confs
        } yield
          s"$k:\t$v"
      )
      .mkString("\t", "\n\t", "")
  )

case object NoSparkContextToClear extends IllegalStateException
