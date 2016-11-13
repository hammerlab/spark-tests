package org.hammerlab.magic.test.rdd

import java.io.{File, FilenameFilter}

import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.filefilter.PrefixFileFilter
import org.apache.spark.rdd.RDD
import org.hammerlab.magic.test.spark.SparkSuite

import scala.reflect.ClassTag

/**
 * Base-trait for tests that check round-trip correctness and on-disk sizes for a given RDD-serde implementation.
 */
trait VerifyRDDSerde
  extends SparkSuite {

  // Subclasses implement serializing and deserializing an RDD.
  protected def serializeRDD[T: ClassTag](rdd: RDD[T], path: String): RDD[T]
  protected def deserializeRDD[T: ClassTag](path: String): RDD[T]

  protected def verifyRDDSerde[T: ClassTag](elems: Seq[T]): Unit =
    verifyFileSizesAndSerde(elems)

  /**
   * Make an rdd out of `elems`, write it to disk, verify the written partitions' sizes match `fileSizes`, read it
   * back in, verify the contents are the same.
   *
   * @param fileSizes if one integer is provided here, assume 4 partitions with that size. If two are provided, assume
   *                  4 partitions where the first's size matches the first integer, and the remaining three match the
   *                  second. Otherwise, the partitions' sizes and number must match `fileSizes`.
   */
  protected def verifyFileSizesAndSerde[T: ClassTag](elems: Seq[T],
                                                     fileSizes: Int*): Unit =
    verifyFileSizeListAndSerde[T](elems, fileSizes)

  protected def verifyFileSizeListAndSerde[T: ClassTag](elems: Seq[T],
                                                        origFileSizes: Seq[Int]): Unit = {

    // If one or two "file sizes" were provided, expand them out into an array of length 4.
    val fileSizes: Seq[Int] =
      if (origFileSizes.size == 1)
        Array.fill(4)(origFileSizes.head)
      else if (origFileSizes.size == 2)
        origFileSizes ++ Array(origFileSizes(1), origFileSizes(1))
      else
        origFileSizes

    val numPartitions =
      if (fileSizes.isEmpty)
        4
      else
        fileSizes.size

    val path = tmpPath()

    val rdd = sc.parallelize(elems, numPartitions)

    serializeRDD[T](rdd, path)

    if (fileSizes.nonEmpty) {
      val fileSizeMap =
        fileSizes
          .zipWithIndex
          .map(p => "part-%05d".format(p._2) -> p._1)
          .toMap

      val filter: FilenameFilter = new PrefixFileFilter("part-")

      new File(path)
        .listFiles(filter)
        .map(
          f =>
            FilenameUtils.getBaseName(f.getAbsolutePath) -> f.length
        )
        .toMap should be(fileSizeMap)
    }

    deserializeRDD[T](path).collect() should be(elems.toArray)
  }
}
