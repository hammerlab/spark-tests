package org.hammerlab.magic.test.listener

import org.hammerlab.magic.test.listener.TestSparkListener.Time

trait Temporal {
  var start: Time = _
  var end: Time = _
}

class HasStatus(var status: Status) extends Temporal

trait Status

case object Running extends Status
case object Pending extends Status

trait Completed extends Status {
  def succeeded: Boolean
}

object Succeeded extends Completed {
  override val succeeded: Boolean = true
}

case class Failed(reason: String) extends Completed {
  override val succeeded: Boolean = false
}

