package org.hammerlab.magic.test

import org.scalatest.matchers.{MatchResult, Matcher}

import scala.collection.SortedMap
import scala.collection.mutable.ArrayBuffer

/**
 * Custom [[Matcher]] for [[Seq]]s of key-value pairs. Prints nicely-formatted messages about tuples that differ between
 * one collection and another in specific ways:
 *
 *   - keys (and hance values) present in one and not the other.
 *   - keys that are present in both but with differing values.
 *   - pairs that are present in both but in different orders.
 */
case class SeqMatcher[K: Ordering, V: Ordering](expected: Seq[(K, V)]) extends Matcher[Seq[(K, V)]] {
  override def apply(actual: Seq[(K, V)]): MatchResult = {

    val expectedMap: Map[K, Set[V]] =
      expected
        .groupBy(_._1)
        .mapValues(_.map(_._2).toSet)

    val actualMap: Map[K, Set[V]] =
      actual
        .groupBy(_._1)
        .mapValues(_.map(_._2).toSet)

    val keys = expectedMap.keySet ++ actualMap.keySet

    val errors = ArrayBuffer[String]()
    errors += "Sequences didn't match!"
    errors += ""

    val differingElemsBuilder = SortedMap.newBuilder[K, (String, String)]
    val extraElemsBuilder = SortedMap.newBuilder[K, String]
    val missingElemsBuilder = SortedMap.newBuilder[K, String]
    for {
      key <- keys
      expectedValues = expectedMap.getOrElse(key, Set())
      actualValues = actualMap.getOrElse(key, Set())
      if expectedValues != actualValues

      missingValues = expectedValues.diff(actualValues).toSeq.sorted
      extraValues = actualValues.diff(expectedValues).toSeq.sorted

      extrasStr = extraValues.mkString(",")
      missingsStr = missingValues.mkString(",")
    } {
      (extraValues.nonEmpty, missingValues.nonEmpty) match {
        case (true, true) =>
          differingElemsBuilder += key -> (extrasStr, missingsStr)
        case (true, false) =>
          extraElemsBuilder += key -> extrasStr
        case (false, true) =>
          missingElemsBuilder += key -> missingsStr
        case (false, false) =>
          // Can't get here.
      }
    }

    val differingElems = differingElemsBuilder.result()
    val extraElems = extraElemsBuilder.result()
    val missingElems = missingElemsBuilder.result()

    if (extraElems.nonEmpty || missingElems.nonEmpty || differingElems.nonEmpty) {

      if (extraElems.nonEmpty) {
        errors += s"Extra elems:"
        errors += extraElems.mkString("\t", "\n\t", "\n")
      }

      if (missingElems.nonEmpty) {
        errors += s"Missing elems:"
        errors += missingElems.mkString("\t", "\n\t", "\n")
      }

      if (differingElems.nonEmpty) {
        val diffLines =
          for {
            (k, (actualValue, expectedValue)) <- differingElems
          } yield
            s"$k: actual: $actualValue, expected: $expectedValue"

        errors += s"Differing values:"
        errors += diffLines.mkString("\t", "\n\t", "\n")
      }
    } else if (actual != expected) {
      errors += s"Elements out of order:"
      errors += "Expected:"
      errors += expected.mkString("\t", "\n\t", "\n")
      errors += "Actual:"
      errors += actual.mkString("\t", "\n\t", "\n")
    }

    MatchResult(
      actual == expected,
      errors.mkString("\n"),
      s"$actual matched; was supposed to not."
    )
  }
}

object SeqMatcher {
  def seqMatch[K: Ordering, V: Ordering](expected: Seq[(K, V)]): Matcher[Seq[(K, V)]] = SeqMatcher[K, V](expected)
  def seqMatch[K: Ordering, V: Ordering](expected: Array[(K, V)]): Matcher[Seq[(K, V)]] = SeqMatcher[K, V](expected.toList)
}
