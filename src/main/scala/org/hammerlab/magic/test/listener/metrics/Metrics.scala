package org.hammerlab.magic.test.listener.metrics

import org.apache.spark.executor.TaskMetrics
import org.hammerlab.magic.test.listener.metrics.IO.{Input, Output}

case class Metrics(input: Input = IO(),
                   output: Output = IO(),
                   shuffleRead: ShuffleRead = ShuffleRead(),
                   shuffleWrite: ShuffleWrite = ShuffleWrite()) {

  def +(o: Metrics): Metrics =
    Metrics(
      input + o.input,
      output + o.output,
      shuffleRead + o.shuffleRead,
      shuffleWrite + o.shuffleWrite
    )

  def -(o: Metrics): Metrics =
    Metrics(
      input - o.input,
      output - o.output,
      shuffleRead - o.shuffleRead,
      shuffleWrite - o.shuffleWrite
    )

  def max(o: Metrics): Metrics =
    Metrics(
      input max o.input,
      output max o.output,
      shuffleRead max o.shuffleRead,
      shuffleWrite max o.shuffleWrite
    )

  def min(o: Metrics): Metrics =
    Metrics(
      input min o.input,
      output min o.output,
      shuffleRead min o.shuffleRead,
      shuffleWrite min o.shuffleWrite
    )
}

object Metrics {
  def apply(metrics: TaskMetrics): Metrics =
    Metrics(
      Input(metrics),
      Output(metrics),
      ShuffleRead(metrics),
      ShuffleWrite(metrics)
    )

  //def apply(input: Input): Metrics = Metrics(input)
  def apply(output: Output): Metrics = Metrics(output = output)
  def apply(shuffleRead: ShuffleRead): Metrics = new Metrics(shuffleRead = shuffleRead)
  def apply(shuffleWrite: ShuffleWrite): Metrics = new Metrics(shuffleWrite = shuffleWrite)

//  def apply(): Metrics = Metrics(IO(), IO(), ShuffleRead(), ShuffleWrite())
}
