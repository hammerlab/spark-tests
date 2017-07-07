name := "spark-tests"

version := "2.1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  libs.value('paths).copy(revision = "1.1.1-SNAPSHOT"),
  scalatest.value,
  spark.value,
  libs.value('spark_util).copy(revision = "1.2.0-SNAPSHOT"),
  testUtils.value
)

testUtilsVersion := "1.2.4-SNAPSHOT"

// Don't include default parent-plugin test-deps
testDeps := Seq()
