package org.hammerlab.spark.test.suite

import org.hammerlab.kryo.Registrar
import org.hammerlab.kryo.spark.Registrator

/**
 * Base for test-suites that rely on Kryo serialization, including registering classes for serialization in a
 * test-suite-scoped manner.
 */
class KryoSparkSuite[T <: Registrar](registrationRequired: Boolean = true,
                                     referenceTracking: Boolean = false)(
    implicit registrar: Registrar = null
  )
  extends SparkSuite
    with Registrator {

  sparkConf(
    // Register this class as its own KryoRegistrator
    "spark.kryo.registrator" → getClass.getCanonicalName,
    "spark.serializer" → "org.apache.spark.serializer.KryoSerializer",
    "spark.kryo.referenceTracking" → referenceTracking.toString,
    "spark.kryo.registrationRequired" → registrationRequired.toString
  )

  /**
   * In addition to any test-suite-specific registrations subclasses add with the [[register]] method, add
   * [[registrar]]'s registrations by default.
   */
  Option(registrar).foreach(register(_))
}
