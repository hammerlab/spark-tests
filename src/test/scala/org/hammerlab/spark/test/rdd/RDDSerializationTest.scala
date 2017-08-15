package org.hammerlab.spark.test.rdd
import java.io.{ ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream }

import org.apache.hadoop.io.{ BytesWritable, NullWritable }
import org.apache.hadoop.mapred.SequenceFileOutputFormat
import org.apache.spark.rdd.RDD
import org.hammerlab.hadoop.splits.UnsplittableSequenceFileInputFormat
import org.hammerlab.paths.Path

import scala.reflect.ClassTag

class RDDSerializationTest
  extends RDDSerialization {
  override protected def serializeRDD[T: ClassTag](rdd: RDD[T],
                                                   path: Path): RDD[T] = {
    rdd
      .map {
        t ⇒
          NullWritable.get → {
            val bos = new ByteArrayOutputStream()
            val oos = new ObjectOutputStream(bos)
            oos.writeObject(t)
            oos.close()
            new BytesWritable(bos.toByteArray)
          }
      }
      .saveAsHadoopFile[
        SequenceFileOutputFormat[
          NullWritable,
          BytesWritable
        ]
      ](
        path.toString
      )

    rdd
  }

  override protected def deserializeRDD[T: ClassTag](path: Path): RDD[T] =
    sc
      .hadoopFile[
        NullWritable,
        BytesWritable,
        UnsplittableSequenceFileInputFormat[
          NullWritable,
          BytesWritable
        ]
      ](
        path.toString
      )
      .values
      .map {
        bytes ⇒
          val bis = new ByteArrayInputStream(bytes.getBytes)
          val ois = new ObjectInputStream(bis)
          val t = ois.readObject().asInstanceOf[T]
          ois.close()
          t
      }

  test("ints round trip") {
    verifyFileSizesAndSerde(1 to 1000, 23565)
  }
}
