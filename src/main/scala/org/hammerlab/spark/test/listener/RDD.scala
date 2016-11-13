package org.hammerlab.spark.test.listener

import org.hammerlab.spark.test.listener.TestSparkListener.RddId

case class RDD(id: RddId, name: String, numPartitions: Int, callSite: String, parents: Seq[RddId])
