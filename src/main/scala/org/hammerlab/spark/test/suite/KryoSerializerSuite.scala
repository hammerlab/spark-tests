package org.hammerlab.spark.test.suite

import com.esotericsoftware.kryo.{Kryo, Serializer}
import org.apache.spark.serializer.KryoRegistrator

import scala.collection.mutable.ArrayBuffer

class KryoSerializerSuite[T <: KryoRegistrator](registrar: Class[T] = null,
                                                registrationRequired: Boolean = true,
                                                referenceTracking: Boolean = false)
  extends SparkSuite
    with KryoRegistrator {

  // Glorified union type for String ∨ Class[_].
  trait RegisterClass {
    def clazz: Class[_]
  }

  // RegisterClass represented as a String.
  implicit class ClassNameToRegister(className: String) extends RegisterClass {
    override def clazz: Class[_] = Class.forName(className)
  }

  // RegisterClass represented as a Class[_].
  implicit class ClassToRegister(val clazz: Class[_]) extends RegisterClass

  private val extraKryoRegistrations = ArrayBuffer[(Class[_], Option[Serializer[_]])]()

  def kryoRegister[T](cls: Class[T], serializer: Serializer[T]): Unit =
    extraKryoRegistrations += cls -> Some(serializer)

  // Subclasses can record extra Kryo classes to register here.
  def kryoRegister(classes: RegisterClass*): Unit =
    extraKryoRegistrations ++= classes.map(_.clazz -> None)

  override def registerClasses(kryo: Kryo): Unit = {
    Option(registrar).foreach(_.newInstance().registerClasses(kryo))
    for {
      (clazz, serializerOpt) <- extraKryoRegistrations
    } {
      serializerOpt match {
        case Some(serializer) => kryo.register(clazz, serializer)
        case None => kryo.register(clazz)
      }
    }
  }

  conf
    .set("spark.kryo.referenceTracking", referenceTracking.toString)
    .set("spark.kryo.registrationRequired", registrationRequired.toString)
    .set("spark.kryo.registrator", getClass.getCanonicalName)
    .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
}
