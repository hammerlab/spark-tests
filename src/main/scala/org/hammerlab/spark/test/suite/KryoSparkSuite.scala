package org.hammerlab.spark.test.suite

import org.hammerlab.kryo.spark.Registrator
import org.scalatest.BeforeAndAfterAll

/**
 * Base for test-suites that rely on Kryo serialization, including registering classes for serialization in a
 * test-suite-scoped manner.
 */
class KryoSparkSuite(override val registrationRequired: Boolean = true,
                     override val referenceTracking: Boolean = false)
  extends SparkSuite
    with Registrator
    with KryoConfs {

  sparkConf(
    "spark.kryo.registrator" → getClass.getCanonicalName
  )
}

trait KryoConfs {
  self: SparkConfBase ⇒

  def registrationRequired: Boolean = true
  def referenceTracking: Boolean = false

  sparkConf(
    "spark.serializer" → "org.apache.spark.serializer.KryoSerializer",
    "spark.kryo.referenceTracking" → referenceTracking.toString,
    "spark.kryo.registrationRequired" → registrationRequired.toString
  )
}
