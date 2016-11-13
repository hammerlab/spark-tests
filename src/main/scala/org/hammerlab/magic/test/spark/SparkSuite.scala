package org.hammerlab.magic.test.spark

import com.holdenkarau.spark.testing.SharedSparkContext
import org.hammerlab.magic.test.TmpFiles
import org.scalatest.{BeforeAndAfterAll, FunSuite, Matchers}

trait SparkSuite
  extends FunSuite
    with Matchers
    with SharedSparkContext
    with BeforeAndAfterAll
    with TmpFiles {

  implicit lazy val sparkContext = sc

  // Set this explicitly so that we get deterministic behavior across test-machines with varying numbers of cores.
  conf
    .setMaster("local[4]")
    .set("spark.app.name", this.getClass.getName)
    .set("spark.driver.allowMultipleContexts", "true")
    .set("spark.driver.host", "localhost")

  // Set checkpoints dir so that tests that use RDD.checkpoint don't fail.
  override def beforeAll(): Unit = {
    super.beforeAll()
    val checkpointsDir = tmpDir()
    sc.setCheckpointDir(checkpointsDir.toString)
  }
}
