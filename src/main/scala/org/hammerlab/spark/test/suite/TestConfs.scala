package org.hammerlab.spark.test.suite

import org.hammerlab.spark.SparkConfBase

trait TestConfs {
  self: SparkConfBase ⇒

  protected def numCores: Int = 4

  sparkConf(
    // Set this explicitly so that we get deterministic behavior across test-machines with varying numbers of cores.
    "spark.master" → s"local[$numCores]",
    "spark.app.name" → this.getClass.getName,
    "spark.driver.host" → "localhost",
    "spark.ui.enabled" → "false",
    "spark.eventLog.enabled" → "false"
  )
}
