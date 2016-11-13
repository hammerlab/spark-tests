package org.hammerlab.magic.test

import org.scalatest.{FunSuite, Matchers}

class SeqMatcherTest extends FunSuite with Matchers {

  def check[K: Ordering, V: Ordering](expected: (K, V)*)(actual: (K, V)*)(mismatchMessageRaw: String = ""): Unit = {
    val matchResult = SeqMatcher(expected).apply(actual)

    val mismatchMessage = mismatchMessageRaw.stripMargin
    val shouldMatch = mismatchMessage.isEmpty
    matchResult.matches should be(shouldMatch)
    if (!shouldMatch) {
      matchResult.failureMessage should be(mismatchMessage)
    }
  }

  test("empty match") {
    check[String, Int]()()()
  }

  test("single match") {
    check(
      "a" -> 1
    )(
      "a" -> 1
    )()
  }

  test("multiple match") {
    check(
      "a" -> 1,
      "b" -> 2,
      "c" -> 3
    )(
      "a" -> 1,
      "b" -> 2,
      "c" -> 3
    )()
  }

  test("single differing key") {
    check(
      "a" -> 1,
      "b" -> 2,
      "c" -> 3
    )(
      "a" -> 1,
      "b" -> 4,
      "c" -> 3
    )(
      """Sequences didn't match!
        |
        |Differing values:
        |	b: actual: 4, expected: 2
        |"""
    )
  }

  test("multiple differing keys") {
    check(
      "a" -> 1,
      "b" -> 2,
      "c" -> 3
    )(
      "a" -> 1,
      "b" -> 4,
      "c" -> 5
    )(
      """Sequences didn't match!
        |
        |Differing values:
        |	b: actual: 4, expected: 2
        |	c: actual: 5, expected: 3
        |"""
    )
  }

  test("empty vs single elem") {
    check()("a" -> 1)(
      """Sequences didn't match!
        |
        |Extra elems:
        |	a -> 1
        |"""
    )
  }

  test("single elem vs empty") {
    check("a" -> 1)()(
      """Sequences didn't match!
        |
        |Missing elems:
        |	a -> 1
        |"""
    )
  }

  test("missing and extra elems") {
    check(
      "a" -> 1,
      "b" -> 2,
      "c" -> 3
    )(
      "d" -> 4,
      "a" -> 1,
      "e" -> 5
    )(
      """Sequences didn't match!
        |
        |Extra elems:
        |	d -> 4
        |	e -> 5
        |
        |Missing elems:
        |	b -> 2
        |	c -> 3
        |"""
    )
  }

  test("out of order elems") {
    check(
      "a" -> 1,
      "b" -> 2
    )(
      "b" -> 2,
      "a" -> 1
    )(
      """Sequences didn't match!
        |
        |Elements out of order:
        |Expected:
        |	(a,1)
        |	(b,2)
        |
        |Actual:
        |	(b,2)
        |	(a,1)
        |"""
    )
  }

  test("multi-valued match") {
    check(
      "a" -> 1,
      "b" -> 2,
      "a" -> 3
    )(
      "a" -> 1,
      "b" -> 2,
      "a" -> 3
    )()
  }

  test("multi-valued mismatch") {
    check(
      "a" -> 1,
      "b" -> 2,
      "a" -> 3
    )(
      "a" -> 1,
      "b" -> 2,
      "a" -> 4
    )(
      """Sequences didn't match!
        |
        |Differing values:
        |	a: actual: 4, expected: 3
        |"""
    )
  }

  test("multi-valued missing") {
    check(
      "a" -> 1,
      "b" -> 2,
      "a" -> 3
    )(
      "b" -> 2
    )(
      """Sequences didn't match!
        |
        |Missing elems:
        |	a -> 1,3
        |"""
    )
  }

  test("multi-valued extra") {
    check(
      "b" -> 2
    )(
      "a" -> 1,
      "b" -> 2,
      "a" -> 4
    )(
      """Sequences didn't match!
        |
        |Extra elems:
        |	a -> 1,4
        |"""
    )
  }

  test("out of order") {
    check(
      "a" -> 1,
      "b" -> 2,
      "a" -> 3
    )(
      "b" -> 2,
      "a" -> 1,
      "a" -> 3
    )(
      """Sequences didn't match!
        |
        |Elements out of order:
        |Expected:
        |	(a,1)
        |	(b,2)
        |	(a,3)
        |
        |Actual:
        |	(b,2)
        |	(a,1)
        |	(a,3)
        |"""
    )
  }
}
