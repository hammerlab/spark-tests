package org.hammerlab.kryo

import com.esotericsoftware.kryo.{ Kryo, Serializer }
import org.apache.spark.serializer.KryoRegistrator

/**
 * Base for (usually implicitly-created) kryo-registrations that subclasses can add.
 */
sealed trait Registration {
  def register(implicit kryo: Kryo): Unit
}

object Registration {

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
   * Compose all of a provided [[Registrar]]'s [[Registration]]'s with this [[Registrar]].
   */
  implicit class RegistrarToRegister(registrar: Registrar) extends Registration {
    override def register(implicit kryo: Kryo): Unit = registrar(kryo)
  }

  /**
   * Compose all of a provided [[KryoRegistrator]]'s [[Registration]]'s with this [[Registrar]].
   */
  implicit class RegistratorToRegister(registrator: KryoRegistrator) extends Registration {
    override def register(implicit kryo: Kryo): Unit = registrator.registerClasses(kryo)
  }

  /**
   * Convenience function, supports syntax like:
   *
   * register(classOf[Foo] → new FooSerializer)
   */
  implicit def makeRegistration[U](t: (Class[U], Serializer[U])): ClassWithSerializerToRegister[U] =
    ClassWithSerializerToRegister(t._1, t._2)
}
