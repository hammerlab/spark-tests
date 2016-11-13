package org.hammerlab.magic.test.version

import scala.util.Properties

/**
 * Simple utilities for the (hopefully rare) case where a test needs to do something different based on what Scala
 * minor-version (2.10, 2.11, and 2.12 are supported here) it is running under.
 */
object Util {
  // cf. http://www.scala-lang.org/old/node/7532.html#comment-31160.
  lazy val version: String =
    Properties.scalaPropOrNone("version.number") match {
      case Some(versionNumber) => versionNumber
      case None => throw new Exception("No Scala 'version.number' property set")
    }

  lazy val versionPrefix = {
    if (version.startsWith("2.10")) "2.10"
    else if (version.startsWith("2.11")) "2.11"
    else if (version.startsWith("2.12")) "2.12"
    else throw new Exception(s"Unrecognized Scala version: $version")
  }

  lazy val is2_10 = versionPrefix == "2.10"
  lazy val is2_11 = versionPrefix == "2.11"
  lazy val is2_12 = versionPrefix == "2.12"
}
