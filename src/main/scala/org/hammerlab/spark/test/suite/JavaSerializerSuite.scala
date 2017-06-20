package org.hammerlab.spark.test.suite

/**
 * Mix-in that sets Spark serialization to Java.
 */
trait JavaSerializerSuite
  extends SparkSuite {
  sparkConf(
    "spark.serializer" → "org.apache.spark.serializer.JavaSerializer"
  )
}
