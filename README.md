# spark-tests

[![Build Status](https://travis-ci.org/hammerlab/spark-tests.svg?branch=master)](https://travis-ci.org/hammerlab/spark-tests)
[![Coverage Status](https://coveralls.io/repos/github/hammerlab/spark-tests/badge.svg)](https://coveralls.io/github/hammerlab/spark-tests)
[![Maven Central](https://img.shields.io/maven-central/v/org.hammerlab/spark-tests_2.11.svg?maxAge=1800)](http://search.maven.org/#search%7Cga%7C1%7Cspark-tests)

Utilities for writing tests that use Apache Spark.

- Give your tests fresh `SparkContext`s:
  - per-`Suite`: [`SparkSuite`](https://github.com/hammerlab/spark-tests/blob/master/src/main/scala/org/hammerlab/spark/test/suite/SparkSuite.scala) (a thin wrapper around [holdenk/spark-testing-base](https://github.com/holdenk/spark-testing-base)'s [`SharedSparkContext`](https://github.com/holdenk/spark-testing-base/blob/v0.3.3/src/main/1.3/scala/com/holdenkarau/spark/testing/SharedSparkContext.scala))
  - per-test-case: [`PerCaseSuite`](https://github.com/hammerlab/spark-tests/blob/master/src/main/scala/org/hammerlab/spark/test/suite/PerCaseSuite.scala).
- [`KryoSparkSuite`](https://github.com/hammerlab/spark-tests/blob/master/src/main/scala/org/hammerlab/spark/test/suite/KryoSparkSuite.scala): `SparkSuite` implementation that provides hooks for kryo-registration.
  - Useful for subclassing once per-project and filling in that project's default Kryo registrar, then having concrete tests subclass that.
  - cf. [hammerlab/guacamole](https://github.com/hammerlab/guacamole/blob/9d330aeb3a7a040c174b851511f19b42d7717508/src/test/scala/org/hammerlab/guacamole/util/GuacFunSuite.scala), [hammerlab/pageant](https://github.com/ryan-williams/pageant/blob/d063db292cad3c68222c38c964d7dda3c7258720/src/test/scala/org/hammerlab/pageant/utils/PageantSuite.scala).
- [`rdd.Util`](https://github.com/hammerlab/spark-tests/blob/master/src/main/scala/org/hammerlab/spark/test/rdd/Util.scala): make an RDD with specific elements in specific partitions.
- [`NumJobsUtil`](https://github.com/hammerlab/spark-tests/blob/master/src/main/scala/org/apache/spark/scheduler/test/NumJobsUtil.scala): verify the number of Spark jobs that have been run.
- [`RDDSerialization`](https://github.com/hammerlab/spark-tests/blob/master/src/main/scala/org/hammerlab/spark/test/rdd/RDDSerialization.scala): interface that allows for verifying that performing a serialization+deserialization round-trip on an RDD results in the same RDD.
