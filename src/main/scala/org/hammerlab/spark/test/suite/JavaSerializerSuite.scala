package org.hammerlab.spark.test.suite

import org.apache.spark.serializer.JavaSerializer

/**
 * Mix-in that sets Spark serialization to Java.
 */
trait JavaSerializerSuite
  extends SparkSuite {
  sparkConf(
    "spark.serializer" → classOf[JavaSerializer].getCanonicalName
  )
}
