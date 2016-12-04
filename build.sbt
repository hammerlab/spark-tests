name := "spark-tests"

version := "1.2.0"

providedDeps += libraries.value('spark)

libraryDependencies ++= Seq(
  libraries.value('scalatest),
  libraries.value('spark_testing_base),
  libraries.value('spark_util),
  libraries.value('test_utils)
)
