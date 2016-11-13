package org.hammerlab.magic.test

import java.io.File
import java.nio.file.{Files, Paths}

import org.apache.commons.io.FileUtils
import org.scalatest.{BeforeAndAfterAll, Suite}

import scala.collection.mutable.ArrayBuffer

/**
 * Mix-in for tests that allows for creating temporary files and directories, and cleans them up before exiting.
 */
trait TmpFiles extends BeforeAndAfterAll {
  self: Suite =>

  val files = ArrayBuffer[String]()
  val dirs = ArrayBuffer[String]()

  def tmpFile(prefix: String = this.getClass.getSimpleName, suffix: String = ""): String = {
    val f = File.createTempFile(prefix, suffix).toString
    files += f
    f
  }

  def tmpDir(prefix: String = this.getClass.getSimpleName): String = {
    val f = Files.createTempDirectory(prefix).toString
    dirs += f
    f
  }

  def tmpPath(prefix: String = this.getClass.getSimpleName, suffix: String = ""): String = {
    Paths.get(tmpDir(), prefix + suffix).toString
  }

  override def afterAll(): Unit = {
    super.afterAll()

    for {
      f <- files
    } {
      Files.delete(Paths.get(f))
    }

    for {
      d <- dirs
    } {
      FileUtils.deleteDirectory(new File(d))
    }
  }
}
