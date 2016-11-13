package org.hammerlab.magic.test.listener

import org.apache.spark.scheduler.StageInfo
import org.hammerlab.magic.test.listener.TestSparkListener.{App, JobId, RddId, StageAttemptId, StageId}

import scala.collection.mutable

case class Stage(app: App, id: StageId, name: String, details: String) {
  var jobId: JobId = _
  val attempts = mutable.HashMap[StageAttemptId, StageAttempt]()
  val rdds = mutable.HashMap[RddId, RDD]()

  def getAttemptFromInfo(stageAttemptInfo: StageInfo): StageAttempt = {
    val stageAttemptId = stageAttemptInfo.attemptId

    val stageAttempt = attempts.getOrElseUpdate(stageAttemptId, StageAttempt(app, this, stageAttemptId))

    for { rddInfo <- stageAttemptInfo.rddInfos } {
      app.handleRDDInfo(rddInfo, this)
    }
    stageAttempt
  }
}

