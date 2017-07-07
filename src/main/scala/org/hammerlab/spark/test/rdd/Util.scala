package org.hammerlab.spark.test.rdd

import org.apache.spark.rdd.RDD
import org.hammerlab.spark.{ Context, KeyPartitioner }

import scala.reflect.ClassTag

/**
 * Make an RDD where the provided elements reside in specific partitions, for testing purposes.
 */
object Util {
  def makeRDD[T: ClassTag](partitions: Iterable[T]*)(implicit sc: Context): RDD[T] =
    sc
      .parallelize(
        for {
          (elems, partition) ← partitions.zipWithIndex
          (elem, idx) ← elems.zipWithIndex
        } yield {
          (partition, idx) → elem
        }
      )
      .repartitionAndSortWithinPartitions(KeyPartitioner(partitions.size))
      .values
}
