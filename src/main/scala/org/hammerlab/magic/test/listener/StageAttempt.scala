package org.hammerlab.magic.test.listener

import org.apache.spark.scheduler.TaskInfo
import org.hammerlab.magic.test.listener.TestSparkListener.{App, StageAttemptId, TaskId}
import org.hammerlab.magic.test.listener.metrics.Metrics

import scala.collection.mutable

case class StageAttempt(app: App, stage: Stage, id: StageAttemptId) extends HasStatus(Pending) {

  def name = stage.name
  def details = stage.details

  val tasks = mutable.HashMap[TaskId, Task]()

  def getTaskAttempt(taskInfo: TaskInfo): TaskAttempt = {
    val task = tasks.getOrElseUpdate(taskInfo.index, Task(this, taskInfo.index))
    task.getTaskAttempt(taskInfo.taskId, taskInfo.attemptNumber)
  }

  var total = Metrics()
  var taskMaxs = Metrics()

  def updateMetrics(taskDelta: Metrics, delta: Metrics) = {
    total += delta
    taskMaxs += taskDelta

    app.updateMetrics(delta)
  }
}

