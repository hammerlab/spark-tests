package org.hammerlab.spark.test.suite

import org.apache.spark.serializer.KryoRegistrator
import org.hammerlab.kryo.spark.Registrator
import org.hammerlab.spark.KryoConfs

/**
 * Base for test-suites that rely on Kryo serialization, including registering classes for serialization in a
 * test-suite-scoped manner.
 */
class KryoSparkSuite(override val registrationRequired: Boolean = true,
                     override val referenceTracking: Boolean = false)
  extends SparkSuite
    with Registrator
    with KryoConfs {
  override def registrar: Class[_ <: KryoRegistrator] = getClass
}
