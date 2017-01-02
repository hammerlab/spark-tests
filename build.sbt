name := "spark-tests"

version := "1.3.1"

libraryDependencies ++= Seq(
  libs.value('scalatest),
  libs.value('spark),
  libs.value('spark_testing_base),
  libs.value('spark_util),
  libs.value('test_utils)
)

// Don't include default parent-plugin test-deps
testDeps := Seq()
