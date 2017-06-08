name := "spark-tests"

version := "1.3.6"

libraryDependencies ++= Seq(
  libs.value('paths),
  scalatest.value,
  spark.value,
  sparkTestingBase.value,
  libs.value('spark_util),
  testUtils.value
)

// Don't include default parent-plugin test-deps
testDeps := Seq()
