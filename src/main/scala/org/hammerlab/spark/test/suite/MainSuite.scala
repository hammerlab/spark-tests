package org.hammerlab.spark.test.suite

import java.lang.System.{ clearProperty, getProperty, setProperty }

import hammerlab.test.Suite
import org.hammerlab.spark.{ SparkConfBase, confs }

import scala.collection.mutable

/**
 * Base class for tests that run applications that instantiate their own
 * [[org.apache.spark.SparkContext SparkContext]]s.
 *
 * Sets Spark configuration settings, including Kryo-serde with required registration, through system properties.
 */
class MainSuite
  extends Suite
    with SparkConfBase
    with TestConfs
    with confs.Kryo {

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    setSparkProps()
  }

  private val propsSet = mutable.Map[String, String]()
  protected def setSparkProps(): Unit =
    for {
      (k, v) ← sparkConfs
    } {
      propsSet(k) = getProperty(k)
      setProperty(k, v)
    }

  protected def unsetSparkProps(): Unit =
    for {
      (k, v) ← propsSet
    } {
      if (v == null)
        clearProperty(k)
      else
        setProperty(k, v)
    }


  override def afterAll(): Unit = {
    unsetSparkProps()
    super.afterAll()
  }
}
