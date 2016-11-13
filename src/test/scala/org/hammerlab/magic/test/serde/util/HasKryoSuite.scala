package org.hammerlab.magic.test.serde.util

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import org.apache.spark.serializer.KryoSerializer
import org.hammerlab.magic.test.serde.KryoSerialization.{kryoBytes, kryoRead}
import org.hammerlab.magic.test.spark.SparkSuite

import scala.reflect.ClassTag

trait HasKryoSuite
  extends SparkSuite {

  var ks: KryoSerializer = _
  implicit var kryo: Kryo = _

  override def beforeAll(): Unit = {
    super.beforeAll()
    ks = new KryoSerializer(sc.getConf)
    kryo = ks.newKryo()
  }

  def checkKryoRoundTrip[T <: AnyRef : ClassTag](t: T, size: Int, includeClass: Boolean = false): Unit = {
    val bytes = kryoBytes(t, includeClass = includeClass)
    bytes.length should be(size)
    kryoRead[T](bytes, includeClass = includeClass) should be(t)
  }

  def checkKryoRoundTrip[T <: AnyRef : ClassTag](size: Int, ts: T*): Unit =
    checkKryoRoundTrip(size, includeClass = false, ts: _*)

  def checkKryoRoundTrip[T <: AnyRef : ClassTag](size: Int, includeClass: Boolean, ts: T*): Unit = {
    val bytes =
      for {
        t <- ts.toArray
        byte <- kryoBytes(t, includeClass = includeClass)
      } yield
        byte

    bytes.length should be(size)

    val ip = new Input(bytes)

    for {
      t <- ts
    } {
      kryoRead[T](ip, includeClass = includeClass) should be(t)
    }

    ip.close()
  }
}
