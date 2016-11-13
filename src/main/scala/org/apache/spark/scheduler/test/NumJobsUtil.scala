package org.apache.spark.scheduler.test

import org.apache.spark.SparkContext

/**
 * Package-cheat to access DAGScheduler.nextJobId, the number of Spark jobs that have been run, for testing purposes.
 */
trait NumJobsUtil {
  def numJobs()(implicit sc: SparkContext): Int =
    sc.dagScheduler.nextJobId.get()
}
