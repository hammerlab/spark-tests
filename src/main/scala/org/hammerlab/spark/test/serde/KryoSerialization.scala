package org.hammerlab.spark.test.serde

import java.io.{ ByteArrayOutputStream, InputStream, OutputStream }

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{ Input, Output }
import org.hammerlab.paths.Path

import scala.reflect.ClassTag

/**
 * Helpers for kryo serde in tests.
 */
object KryoSerialization {
  def kryoRead[T: ClassTag](bytes: Array[Byte])(
      implicit kryo: Kryo
  ): T =
    kryoRead[T](bytes, includeClass = false)

  def kryoRead[T: ClassTag](bytes: Array[Byte],
                            includeClass: Boolean)(
      implicit kryo: Kryo
  ): T =
    kryoRead[T](new Input(bytes), includeClass)


  def kryoRead[T: ClassTag](is: InputStream)(implicit kryo: Kryo): T =
    kryoRead[T](is, includeClass = false)

  def kryoRead[T: ClassTag](is: InputStream,
                            includeClass: Boolean)(
      implicit kryo: Kryo
  ): T = {
    val ip = new Input(is)
    try {
      kryoRead[T](ip, includeClass)
    } finally {
      ip.close()
    }
  }

  def kryoRead[T: ClassTag](path: Path)(implicit kryo: Kryo): T =
    kryoRead[T](path, includeClass = false)

  def kryoRead[T: ClassTag](path: Path,
                  includeClass: Boolean)(
      implicit kryo: Kryo
  ): T =
    kryoRead[T](
      path.inputStream,
      includeClass
    )

  def kryoRead[T](ip: Input,
                  includeClass: Boolean)(
      implicit
      ct: ClassTag[T],
      kryo: Kryo
  ): T =
    if (includeClass) {
      kryo
        .readClassAndObject(ip)
        .asInstanceOf[T]
    } else {
      kryo
        .readObject(ip, ct.runtimeClass)
        .asInstanceOf[T]
    }

  def kryoWrite(o: Object,
                os: OutputStream,
                includeClass: Boolean)(
      implicit kryo: Kryo
  ): Unit = {
    val op = new Output(os)
    if (includeClass) {
      kryo.writeClassAndObject(op, o)
    } else {
      kryo.writeObject(op, o)
    }
    op.close()
  }

  def kryoWrite(o: Object, out: Path)(implicit kryo: Kryo): Unit =
    kryoWrite(o, out, includeClass = false)

  def kryoWrite(o: Object, out: Path, includeClass: Boolean)(implicit kryo: Kryo): Unit =
    kryoWrite(o, out.outputStream, includeClass)

  def kryoBytes(o: Object)(implicit kryo: Kryo): Array[Byte] =
    kryoBytes(o, includeClass = false)

  def kryoBytes(o: Object, includeClass: Boolean)(implicit kryo: Kryo): Array[Byte] = {
    val baos = new ByteArrayOutputStream()
    kryoWrite(o, baos, includeClass)
    baos.toByteArray
  }
}
