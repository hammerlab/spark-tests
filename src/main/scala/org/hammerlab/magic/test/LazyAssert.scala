package org.hammerlab.magic.test

import org.scalatest.Assertions

trait LazyAssert {
  self: Assertions =>

  def lazyAssert(predicate: Boolean, msg: => String): Unit = {
    assert(predicate, LazyMessage(msg))
  }
}

class LazyMessage(msg: => String) {
  override def toString: String = msg
}

object LazyMessage {
  def apply(msg: => String): LazyMessage = new LazyMessage(msg)
}
