package org.hammerlab.magic.test.listener

import org.hammerlab.magic.test.listener.TestSparkListener.{TaskAttemptId, TaskAttemptNum, TaskIndex}
import org.hammerlab.magic.test.listener.metrics.Metrics

import scala.collection.mutable

case class Task(stageAttempt: StageAttempt, index: TaskIndex) {
  def stage = stageAttempt.stage
  def app = stage.app
  val attempts = mutable.HashMap[TaskAttemptId, TaskAttempt]()

  var maxMetrics = Metrics()
  var totalMetrics = Metrics()

  def getTaskAttempt(id: TaskAttemptId, attemptNum: TaskAttemptNum): TaskAttempt =
    attempts.getOrElseUpdate(attemptNum, TaskAttempt(this, id, attemptNum))

  def updateMetrics(newMetrics: Metrics, delta: Metrics) = {
    totalMetrics += delta
    val newMaxMetrics = maxMetrics max newMetrics
    val maxDelta = newMaxMetrics - maxMetrics
    maxMetrics = newMaxMetrics

    stageAttempt.updateMetrics(maxDelta, delta)
  }
}

