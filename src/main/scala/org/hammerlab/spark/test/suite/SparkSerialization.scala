package org.hammerlab.spark.test.suite

import java.nio.ByteBuffer

import org.apache.spark.SparkEnv

/**
 * Mix-in that exposes a Spark [[org.apache.spark.serializer.Serializer]] instance.
 */
trait SparkSerialization {
  self: SparkSuite ⇒

  private lazy val serializer = SparkEnv.get.serializer.newInstance()

  def serialize(item: Any): ByteBuffer = serializer.serialize(item)
  def deserialize[T](bytes: ByteBuffer): T = serializer.deserialize(bytes)
  def deserialize[T](bytes: Array[Byte]): T = deserialize(ByteBuffer.wrap(bytes))

  implicit def byteBufferToArray(byteBuffer: ByteBuffer): Array[Byte] = byteBuffer.array()
}
