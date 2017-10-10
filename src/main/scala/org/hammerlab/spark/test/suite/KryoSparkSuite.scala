package org.hammerlab.spark.test.suite

import org.hammerlab.spark.SelfRegistrar

/**
 * Base for test-suites that rely on Kryo serialization, including registering classes for serialization in a
 * test-suite-scoped manner.
 */
class KryoSparkSuite(override val registrationRequired: Boolean = true,
                     override val referenceTracking: Boolean = false)
  extends SparkSuite
    with SelfRegistrar
