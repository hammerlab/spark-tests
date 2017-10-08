package org.hammerlab.spark.test

import java.util.concurrent.atomic.AtomicLong

import org.apache.spark.util.AccumulatorV2

/**
 * Thread-safe version of [[org.apache.spark.util.LongAccumulator]], suitable for use as a static value in tests.
 *
 * See https://issues.apache.org/jira/browse/SPARK-21425.
 */
case class AtomicLongAccumulator(initialValue: Long = 0)
  extends AccumulatorV2[Long, Long] {
  private var _value = new AtomicLong(initialValue)
  override def value: Long = _value.get
  override def isZero: Boolean = value == 0
  override def copy(): AccumulatorV2[Long, Long] = AtomicLongAccumulator(value)
  override def reset(): Unit = _value = new AtomicLong(0)
  override def add(v: Long): Unit = _value.addAndGet(v)
  override def merge(other: AccumulatorV2[Long, Long]): Unit = add(other.value)
}
