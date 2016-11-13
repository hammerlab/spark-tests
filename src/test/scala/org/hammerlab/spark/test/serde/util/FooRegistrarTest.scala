package org.hammerlab.spark.test.serde.util

import org.hammerlab.spark.test.suite.KryoSerializerSuite

/**
 * Test base-class that registers dummy case-class [[Foo]] for Kryo serde.
 */
class FooRegistrarTest
  extends KryoSerializerSuite(
    registrationRequired = false,
    referenceTracking = true
  ) {
  kryoRegister(classOf[Foo])
}
