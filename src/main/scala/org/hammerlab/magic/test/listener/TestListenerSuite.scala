package org.hammerlab.magic.test.listener

import org.apache.spark.SparkConf
import org.hammerlab.magic.test.spark.PerCaseSparkContext
import org.scalatest.Suite
import org.apache.spark.SparkContext

/**
 * Test mix-in that enforces per-case [[SparkContext]]s with [[TestSparkListener]]s attached.
 *
 * NOTE: not threadsafe! Use with extreme caution, cf. https://github.com/hammerlab/magic-rdds/issues/18.
 */
trait TestListenerSuite extends PerCaseSparkContext {
  self: Suite =>

  var listener: TestSparkListener = _

  def numStages = listener.stages.size

  override protected def setConfigs(conf: SparkConf): Unit = {
    conf.set("spark.extraListeners", "org.hammerlab.magic.test.listener.TestSparkListener")
  }

  override def beforeEach() {
    super.beforeEach()

    listener = TestSparkListener()
    assert(listener != null)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    listener = null
  }
}
