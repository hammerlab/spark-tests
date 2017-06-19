name := "spark-tests"

version := "2.0.0-SNAPSHOT"

libraryDependencies ++= Seq(
  libs.value('paths),
  scalatest.value,
  spark.value,
  libs.value('spark_util),
  testUtils.value
)

// Don't include default parent-plugin test-deps
testDeps := Seq()
