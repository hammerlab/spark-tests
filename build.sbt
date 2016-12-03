name := "spark-tests"

version := "1.1.4-SNAPSHOT"

providedDeps += libraries.value('spark)

libraryDependencies ++= Seq(
  libraries.value('scalatest),
  libraries.value('spark_testing_base),
  libraries.value('spark_util)
)
