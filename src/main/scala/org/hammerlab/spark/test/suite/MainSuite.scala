package org.hammerlab.spark.test.suite

import java.lang.System.setProperty

import org.apache.spark.serializer.KryoRegistrator
import org.hammerlab.spark.{ KryoConfs, SparkConfBase }
import org.hammerlab.test.Suite

/**
 * Base class for tests that run applications that instantiate their own
 * [[org.apache.spark.SparkContext SparkContext]]s.
 *
 * Sets Spark configuration settings, including Kryo-serde with required registration, through system properties.
 */
class MainSuite(override val registrar: Class[_ <: KryoRegistrator] = null)
  extends Suite
    with SparkConfBase
    with TestConfs
    with KryoConfs {

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    setSparkProps()
  }
}
