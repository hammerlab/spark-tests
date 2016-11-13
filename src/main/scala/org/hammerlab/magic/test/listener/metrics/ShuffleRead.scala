package org.hammerlab.magic.test.listener.metrics

import org.apache.spark.executor.{ShuffleReadMetrics, TaskMetrics}

case class ShuffleRead(records: Long = 0,
                       localBytes: Long = 0,
                       localBlocks: Long = 0,
                       remoteBytes: Long = 0,
                       remoteBlocks: Long = 0,
                       fetchWaitTime: Long = 0) {

  def +(o: ShuffleRead): ShuffleRead = combine(o, _ + _)
  def -(o: ShuffleRead): ShuffleRead = combine(o, _ - _)
  def max(o: ShuffleRead): ShuffleRead = combine(o, math.max)
  def min(o: ShuffleRead): ShuffleRead = combine(o, math.min)

  def combine(o: ShuffleRead, fn: (Long, Long) => Long): ShuffleRead =
    ShuffleRead(
      fn(records, o.records),
      fn(localBytes, o.localBytes),
      fn(localBlocks, o.localBlocks),
      fn(remoteBytes, o.remoteBytes),
      fn(remoteBlocks, o.remoteBlocks),
      fn(fetchWaitTime, o.fetchWaitTime)
    )
}

object ShuffleRead {

  def apply(taskMetrics: TaskMetrics): ShuffleRead = ShuffleRead(taskMetrics.shuffleReadMetrics)

  def apply(shuffleReadMetricsOpt: Option[ShuffleReadMetrics]): ShuffleRead =
    shuffleReadMetricsOpt match {
      case Some(shuffleReadMetrics) => ShuffleRead(shuffleReadMetrics)
      case None => ShuffleRead()
    }

  def apply(m: ShuffleReadMetrics): ShuffleRead =
    ShuffleRead(
      m.recordsRead,
      m.localBytesRead,
      m.localBlocksFetched,
      m.remoteBytesRead,
      m.remoteBlocksFetched,
      m.fetchWaitTime
    )
}
