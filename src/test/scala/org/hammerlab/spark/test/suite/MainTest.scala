package org.hammerlab.spark.test.suite

import org.apache.spark.SparkContext
import org.hammerlab.paths.Path
import org.hammerlab.test.linesMatch
import org.hammerlab.test.matchers.lines.Digits
import org.hammerlab.test.matchers.lines.Line._

/**
 * Test a trivial application that writes its [[org.apache.spark.SparkConf]] values to a specified filename.
 */
class MainTest
  extends MainSuite {

  test("main") {
    val outPath = tmpPath()

    Main.main(
      Array(
        outPath.toString()
      )
    )

    outPath.read should linesMatch(
      "spark.app.id local-" ++ Digits,
      "spark.app.name org.hammerlab.spark.test.suite.MainTest",
      "spark.driver.host localhost",
      "spark.driver.port " ++ Digits,
      "spark.eventLog.enabled false",
      "spark.executor.id driver",
      "spark.kryo.referenceTracking false",
      "spark.kryo.registrationRequired true",
      "spark.master local[4]",
      "spark.serializer org.apache.spark.serializer.KryoSerializer",
      "spark.ui.enabled false",
      ""
    )
  }
}

object Main {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext
    val outPath = Path(args(0))
    outPath
      .writeLines(
        sc
          .getConf
          .getAll
          .sortBy(_._1)
          .map {
            case (k, v) ⇒
              s"$k $v"
          }
      )
    sc.stop()
  }
}
