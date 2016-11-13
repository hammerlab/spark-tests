package org.hammerlab.magic.test.serde

import java.io.{ByteArrayOutputStream, FileInputStream, FileOutputStream, InputStream, OutputStream}

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{Input, Output}

import scala.reflect.ClassTag

/**
 * Helpers for kryo serde in tests.
 */
object KryoSerialization {
  def kryoRead[T](bytes: Array[Byte])(implicit ct: ClassTag[T], kryo: Kryo): T = {
    kryoRead[T](bytes, includeClass = false)
  }
  def kryoRead[T](bytes: Array[Byte], includeClass: Boolean)(implicit ct: ClassTag[T], kryo: Kryo): T = {
    kryoRead[T](new Input(bytes), includeClass)
  }

  def kryoRead[T](is: InputStream)(implicit ct: ClassTag[T], kryo: Kryo): T = {
    kryoRead[T](is, includeClass = false)
  }
  def kryoRead[T](is: InputStream, includeClass: Boolean)(implicit ct: ClassTag[T], kryo: Kryo): T = {
    val ip = new Input(is)
    try {
      kryoRead[T](ip, includeClass)
    } finally {
      ip.close()
    }
  }

  def kryoRead[T](fn: String)(implicit ct: ClassTag[T], kryo: Kryo): T = {
    kryoRead[T](fn, includeClass = false)
  }
  def kryoRead[T](fn: String, includeClass: Boolean)(implicit ct: ClassTag[T], kryo: Kryo): T = {
    kryoRead[T](new FileInputStream(fn), includeClass)
  }

  def kryoRead[T](ip: Input, includeClass: Boolean)(implicit ct: ClassTag[T], kryo: Kryo): T = {
    if (includeClass) {
      kryo.readClassAndObject(ip).asInstanceOf[T]
    } else {
      kryo.readObject(ip, ct.runtimeClass).asInstanceOf[T]
    }
  }

  def kryoWrite(o: Object, os: OutputStream, includeClass: Boolean)(implicit kryo: Kryo): Unit = {

    val op = new Output(os)
    if (includeClass) {
      kryo.writeClassAndObject(op, o)
    } else {
      kryo.writeObject(op, o)
    }
    op.close()
  }

  def kryoWrite(o: Object, fn: String)(implicit kryo: Kryo): Unit = kryoWrite(o, fn, includeClass = false)
  def kryoWrite(o: Object, fn: String, includeClass: Boolean)(implicit kryo: Kryo): Unit = {
    kryoWrite(o, new FileOutputStream(fn), includeClass)
  }

  def kryoBytes(o: Object)(implicit kryo: Kryo): Array[Byte] = kryoBytes(o, includeClass = false)
  def kryoBytes(o: Object, includeClass: Boolean)(implicit kryo: Kryo): Array[Byte] = {
    val baos = new ByteArrayOutputStream()
    kryoWrite(o, baos, includeClass)
    baos.toByteArray
  }
}
