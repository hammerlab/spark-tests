package org.hammerlab.test.files

import org.hammerlab.test.Resources
import org.scalatest.Matchers

import scala.io.Source

object Verify extends Matchers {
  def apply(expected: String, actual: String) = {
    val expectedStr = Source.fromFile(Resources.file(expected)).mkString
    val actualStr = Source.fromFile(actual).mkString

    expectedStr should be(actualStr)
  }
}
