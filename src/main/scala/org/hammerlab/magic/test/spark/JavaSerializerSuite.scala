package org.hammerlab.magic.test.spark

/**
 * Mix-in that sets Spark serialization to Java.
 */
trait JavaSerializerSuite extends SparkSuite {
  conf.set("spark.serializer", "org.apache.spark.serializer.JavaSerializer")
}
