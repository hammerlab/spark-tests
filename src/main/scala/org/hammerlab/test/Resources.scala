package org.hammerlab.test

import java.net.URL

object Resources {
  def url(path: String): URL = {
    Thread.currentThread().getContextClassLoader.getResource(path)
  }

  def file(path: String): String = url(path).getFile
}
