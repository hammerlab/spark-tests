package org.hammerlab.spark.test.rdd
import org.apache.spark.rdd.RDD
import org.hammerlab.paths.Path

import scala.reflect.ClassTag

class RDDSerializationTest
  extends RDDSerialization {
  override protected def serializeRDD[T: ClassTag](rdd: RDD[T],
                                                   path: Path): RDD[T] = {
    rdd.saveAsObjectFile(path.toString)
    rdd
  }

  override protected def deserializeRDD[T: ClassTag](path: Path): RDD[T] = {
    sc.objectFile[T](path.toString)
  }

  test("ints round trip") {
    verifyFileSizesAndSerde(1 to 1000, 2070)
  }
}
