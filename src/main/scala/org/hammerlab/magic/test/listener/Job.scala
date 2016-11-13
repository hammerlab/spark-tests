package org.hammerlab.magic.test.listener

import org.hammerlab.magic.test.listener.TestSparkListener.{JobId, StageId, Time}

import scala.collection.mutable

case class Job(id: JobId) extends HasStatus(Running) {
  val stages = mutable.HashMap[StageId, Stage]()
}

object Job {
  def apply(id: JobId, start: Time): Job = {
    val job = Job(id)
    job.start = start
    job
  }
}
