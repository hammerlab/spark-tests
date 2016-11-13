package org.hammerlab.spark.test.suite

import org.scalatest.{FunSuite, Matchers}

trait PerCaseSuite
  extends FunSuite
    with Matchers
    with PerCaseSparkContext
