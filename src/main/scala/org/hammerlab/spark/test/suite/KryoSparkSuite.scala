package org.hammerlab.spark.test.suite

import org.hammerlab.kryo.Registrar
import org.hammerlab.kryo.spark.Registrator

/**
 * Base for test-suites that rely on Kryo serialization, including registering classes for serialization in a
 * test-suite-scoped manner.
 */
class KryoSparkSuite(registrationRequired: Boolean = true,
                     referenceTracking: Boolean = false)
  extends SparkSuite
    with Registrator {

  sparkConf(
    // Register this class as its own KryoRegistrator
    "spark.kryo.registrator" → getClass.getCanonicalName,
    "spark.serializer" → "org.apache.spark.serializer.KryoSerializer",
    "spark.kryo.referenceTracking" → referenceTracking.toString,
    "spark.kryo.registrationRequired" → registrationRequired.toString
  )
}
