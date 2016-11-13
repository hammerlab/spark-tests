package org.hammerlab.magic.test.serde

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, FileInputStream, FileOutputStream, InputStream, ObjectInputStream, ObjectOutputStream, OutputStream}

/**
 * Helpers for Java serde in tests.
 */
object JavaSerialization {
  def javaRead[T](bytes: Array[Byte]): T = {
    javaRead(new ByteArrayInputStream(bytes))
  }
  def javaRead[T](is: InputStream): T = {
    val ois = new ObjectInputStream(is)
    try {
      ois.readObject().asInstanceOf[T]
    } finally {
      ois.close()
    }
  }

  def javaRead[T](fn: String): T = {
    javaRead(new FileInputStream(fn))
  }

  def javaWrite(o: Object, fn: String): Unit = {
    javaWrite(o, new FileOutputStream(fn))
  }

  def javaWrite(o: Object, os: OutputStream): Unit = {
    val oos = new ObjectOutputStream(os)
    oos.writeObject(o)
    oos.close()
  }

  def javaBytes(o: Object): Array[Byte] = {
    val baos = new ByteArrayOutputStream()
    javaWrite(o, baos)
    baos.toByteArray
  }
}
