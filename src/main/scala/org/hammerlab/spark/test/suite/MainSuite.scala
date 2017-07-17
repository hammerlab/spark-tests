package org.hammerlab.spark.test.suite

import java.lang.System.setProperty

import org.apache.spark.serializer.KryoRegistrator
import org.hammerlab.test.Suite

class MainSuite(registrar: Class[_ <: KryoRegistrator] = null)
  extends Suite
    with SparkConfBase
    with KryoConfs {

  setProperty(
    "spark.driver.allowMultipleContexts", "true"
  )

  Option(registrar).foreach(clz ⇒
    setProperty("spark.kryo.registrator", clz.getCanonicalName)
  )

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    setSparkProps()
  }
}
