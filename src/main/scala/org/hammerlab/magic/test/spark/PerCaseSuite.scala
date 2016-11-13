package org.hammerlab.magic.test.spark

import org.scalatest.{FunSuite, Matchers}

trait PerCaseSuite
  extends FunSuite
    with Matchers
    with PerCaseSparkContext
