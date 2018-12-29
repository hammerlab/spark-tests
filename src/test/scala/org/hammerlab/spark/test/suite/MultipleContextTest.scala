package org.hammerlab.spark.test.suite

import org.apache.spark.SparkContext
import uk.org.lidalia.slf4jext.Level.WARN
import uk.org.lidalia.slf4jtest.TestLoggerFactory

import scala.collection.JavaConverters._

class MultipleContextTest
  extends SparkSuite
     with hammerlab.test.cmp.tuple
{

  val _logger = TestLoggerFactory.getTestLogger(getClass)

  test("second ctx") {
    val msgs =
      _logger
        .getLoggingEvents
        .asScala
        .toList
        .map {
          e ⇒
            (
              e.getLevel,
              e.getMessage
            )
        }

    ==(
      msgs,
      (WARN,      "^Previous SparkContext still active! Shutting down; here is the caught error:".r) ::
      (WARN, "^org.apache.spark.SparkException: Only one SparkContext may be running in this JVM".r) ::
      Nil
    )

    ==(sc2.isStopped, true)
    ==(sc.parallelize(1 to 10).count, 10)
  }

  var sc2: SparkContext = _
  override protected def beforeAll(): Unit = {
    sc2 = new SparkContext(makeSparkConf)
    super.beforeAll()
  }

  override def afterAll(): Unit = {
    super.afterAll()
    ==(sc2.isStopped, true)
  }
}
