package org.hammerlab.magic.test.listener

import org.hammerlab.magic.test.listener.TestSparkListener.RddId

case class RDD(id: RddId, name: String, numPartitions: Int, callSite: String, parents: Seq[RddId])
