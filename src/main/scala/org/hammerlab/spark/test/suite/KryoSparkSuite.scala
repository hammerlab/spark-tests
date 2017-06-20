package org.hammerlab.spark.test.suite

import com.esotericsoftware.kryo.{ Kryo, Serializer }
import org.apache.spark.serializer.KryoRegistrator

import scala.collection.mutable.ArrayBuffer

/**
 * Base for test-suites that rely on Kryo serialization, including registering classes for serialization in a
 * test-suite-scoped manner.
 *
 * The [[register]] method allows for tests to register custom classes (and/or additional "registrators" that themselves
 * register classes) that may be serialized in the course of testing, but which generally needn't be registered for
 * production usage.
 */
class KryoSparkSuite[T <: KryoRegistrator](registrar: Class[T] = null,
                                           registrationRequired: Boolean = true,
                                           referenceTracking: Boolean = false)
  extends SparkSuite
    with KryoRegistrator {

  /**
   * Base for (usually implicitly-created) kryo-registrations that subclasses can add.
   */
  sealed trait Registration {
    def register(implicit kryo: Kryo): Unit
  }

  /**
   * Record a sequence of [[Registration]]s for later registration with Kryo, during [[org.apache.spark.SparkEnv]]
   * initialization.
   */
  def register(registrations: Registration*): Unit =
    extraKryoRegistrations ++= registrations

  /**
   * Simple registration: just a [[Class]].
   */
  implicit class ClassToRegister(cls: Class[_]) extends Registration {
    override def register(implicit kryo: Kryo): Unit = kryo.register(cls)
  }

  /**
   * Register a class by (fully-qualified) name.
   */
  implicit class ClassNameToRegister(className: String) extends Registration {
    override def register(implicit kryo: Kryo): Unit = kryo.register(Class.forName(className))
  }

  /**
   * Register a class, along with a custom serializer.
   */
  case class ClassWithSerializerToRegister[U](cls: Class[U], serializer: Serializer[U]) extends Registration {
    override def register(implicit kryo: Kryo): Unit = kryo.register(cls, serializer)
  }

  /**
   * Register an additional [[KryoRegistrator]].
   */
  implicit class RegistratorToRegister(registrator: KryoRegistrator) extends Registration {
    override def register(implicit kryo: Kryo): Unit = registrator.registerClasses(kryo)
  }

  /**
   * Convenience function, supports syntax like:
   *
   *   register(classOf[Foo] → new FooSerializer)
   */
  implicit def makeRegistration[U](t: (Class[U], Serializer[U])): ClassWithSerializerToRegister[U] =
    ClassWithSerializerToRegister(t._1, t._2)

  /**
   * Additional registrations are queued here during instance initialization, and actually registered during
   * [[registerClasses]] (called by Spark during [[org.apache.spark.SparkEnv]] initialization.
   */
  private val extraKryoRegistrations = ArrayBuffer[Registration]()

  /**
   * Spark's hook for registering classes with Kryo.
   */
  override def registerClasses(kryo: Kryo): Unit = {
    Option(registrar).foreach(_.newInstance().registerClasses(kryo))
    for {
      registration ← extraKryoRegistrations.reverseIterator
    } {
      registration.register(kryo)
    }
  }

  sparkConf(
    // Register this class as its own KryoRegistrator
    "spark.kryo.registrator" → getClass.getCanonicalName,
    "spark.serializer" → "org.apache.spark.serializer.KryoSerializer",
    "spark.kryo.referenceTracking" → referenceTracking.toString,
    "spark.kryo.registrationRequired" → registrationRequired.toString
  )
}
