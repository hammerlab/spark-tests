package org.hammerlab.kryo

import com.esotericsoftware.kryo.Kryo

import scala.collection.mutable.ArrayBuffer

trait Registrar {
  def apply(implicit kryo: Kryo): Unit =
    for {
      registration ← extraKryoRegistrations.reverseIterator
    } {
      registration.register(kryo)
    }

  /**
   * Additional registrations are queued here during instance initialization.
   */
  private val extraKryoRegistrations = ArrayBuffer[Registration]()

  def register(registrations: Registration*): Unit =
    extraKryoRegistrations ++= registrations
}

object Registrar {
  /**
   * Record a sequence of [[Registration]]s for later registration with Kryo, during [[org.apache.spark.SparkEnv]]
   * initialization.
   */
  def register(registrations: Registration*)(implicit registrar: Registrar): Unit =
    registrar.register(registrations: _*)
}
