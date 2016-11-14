# spark-tests

[![Build Status](https://travis-ci.org/hammerlab/spark-tests.svg?branch=master)](https://travis-ci.org/hammerlab/spark-tests)
[![Coverage Status](https://coveralls.io/repos/github/hammerlab/spark-tests/badge.svg)](https://coveralls.io/github/hammerlab/spark-tests)
[![Maven Central](https://img.shields.io/maven-central/v/org.hammerlab/spark-tests_2.11.svg?maxAge=25920)](http://search.maven.org/#search%7Cga%7C1%7Cspark-tests)

Utilities for writing tests that use Apache Spark.

- give your tests fresh `SparkContext`s
  - [per-`Suite`](https://github.com/hammerlab/spark-tests/blob/master/src/main/scala/org/hammerlab/test/spark/SparkSuite.scala) (a thin wrapper around [holdenk/spark-testing-base](https://github.com/holdenk/spark-testing-base)'s [`SharedSparkContext`](https://github.com/holdenk/spark-testing-base/blob/v0.3.3/src/main/1.3/scala/com/holdenkarau/spark/testing/SharedSparkContext.scala))
  - or [per-case](https://github.com/hammerlab/spark-tests/blob/master/src/main/scala/org/hammerlab/test/spark/PerCaseSuite.scala)
- [make an RDD with specific elements in specific partitions](https://github.com/hammerlab/spark-tests/blob/master/src/main/scala/org/hammerlab/test/rdd/Util.scala),
- [create and cleaning up temporary files and directories](https://github.com/hammerlab/spark-tests/blob/master/src/main/scala/org/hammerlab/test/TmpFiles.scala)
- [verify the number of Spark jobs that have been run](https://github.com/hammerlab/spark-tests/blob/master/src/main/scala/org/apache/spark/scheduler/test/NumJobsUtil.scala)
