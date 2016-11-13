package org.hammerlab.magic.test.listener

import org.apache.spark.executor.TaskMetrics
import org.hammerlab.magic.test.listener.TestSparkListener.{TaskAttemptId, TaskAttemptNum}
import org.hammerlab.magic.test.listener.metrics.Metrics

case class TaskAttempt(task: Task, id: TaskAttemptId, attempt: TaskAttemptNum) extends HasStatus(Running) {
  def stageAttempt = task.stageAttempt
  def stage = stageAttempt.stage
  def app = stage.app

  var metrics: Metrics = Metrics()

  def updateMetrics(taskMetrics: TaskMetrics): Unit = {
    val newMetrics = Metrics(taskMetrics)
    val delta = newMetrics - metrics
    task.updateMetrics(newMetrics, delta)
  }
}

