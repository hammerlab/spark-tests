package org.hammerlab.magic.test.listener.metrics

import org.apache.spark.executor.{ShuffleWriteMetrics, TaskMetrics}

case class ShuffleWrite(records: Long = 0, bytes: Long = 0, time: Long = 0) {

  def +(o: ShuffleWrite): ShuffleWrite = combine(o, _ + _)
  def -(o: ShuffleWrite): ShuffleWrite = combine(o, _ - _)
  def max(o: ShuffleWrite): ShuffleWrite = combine(o, math.max)
  def min(o: ShuffleWrite): ShuffleWrite = combine(o, math.min)

  def combine(o: ShuffleWrite, fn: (Long, Long) => Long): ShuffleWrite =
    ShuffleWrite(
      fn(records, o.records),
      fn(bytes, o.bytes),
      fn(time, o.time)
    )

  override def equals(o: Any): Boolean =
    o match {
      case s: ShuffleWrite => bytes == s.bytes && records == s.records
      case _ => false
    }
}

object ShuffleWrite {
  def apply(taskMetrics: TaskMetrics): ShuffleWrite = ShuffleWrite(taskMetrics.shuffleWriteMetrics)

  def apply(shuffleWriteMetricsOpt: Option[ShuffleWriteMetrics]): ShuffleWrite =
    shuffleWriteMetricsOpt match {
      case Some(shuffleWriteMetrics) => ShuffleWrite(shuffleWriteMetrics)
      case None => ShuffleWrite()
    }

  def apply(m: ShuffleWriteMetrics): ShuffleWrite =
    ShuffleWrite(
      m.shuffleRecordsWritten,
      m.shuffleBytesWritten,
      m.shuffleWriteTime
    )
}
