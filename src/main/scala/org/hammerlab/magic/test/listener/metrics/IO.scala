package org.hammerlab.magic.test.listener.metrics

import org.apache.spark.executor.{InputMetrics, OutputMetrics, TaskMetrics}
import org.hammerlab.magic.test.listener.metrics.IO.{Input, Output}

case class IO(bytes: Long, records: Long) {
  def combine(o: IO, fn: (Long, Long) => Long): IO = IO(fn(bytes, o.bytes), fn(records, o.records))
  def +(o: IO): IO = combine(o, _ + _)
  def -(o: IO): IO = combine(o, _ - _)
  def max(o: IO): IO = combine(o, math.max)
  def min(o: IO): IO = combine(o, math.min)
}

object IO {
  type Input = IO
  type Output = IO

  def apply(): IO = IO(0, 0)
}

object Input {
  def apply(taskMetrics: TaskMetrics): Input = Input(taskMetrics.inputMetrics)

  def apply(inputMetricsOpt: Option[InputMetrics]): IO =
    inputMetricsOpt match {
      case Some(inputMetrics) => Input(inputMetrics)
      case None => IO(0, 0)
    }

  def apply(inputMetrics: InputMetrics): IO = IO(inputMetrics.bytesRead, inputMetrics.recordsRead)
}

object Output {
  def apply(taskMetrics: TaskMetrics): Output = Output(taskMetrics.outputMetrics)

  def apply(outputMetricsOpt: Option[OutputMetrics]): Output =
    outputMetricsOpt match {
      case Some(outputMetrics) => Output(outputMetrics)
      case None => IO()
    }

  def apply(outputMetrics: OutputMetrics): Output = IO(outputMetrics.bytesWritten, outputMetrics.recordsWritten)
}
