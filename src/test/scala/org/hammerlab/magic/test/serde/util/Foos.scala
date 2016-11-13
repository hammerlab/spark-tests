package org.hammerlab.magic.test.serde.util

import scala.collection.immutable.StringOps

/**
 * Helper for making a bunch of random-ish [[Foo]]s.
 */
object Foos {
  def apply(n: Int, k: Int = 8): Seq[Foo] = {
    (1 to n).map(i => {
      val ch = ((i%26) + 96).toChar
      Foo(i, new StringOps(ch.toString) * k)
    })
  }
}
