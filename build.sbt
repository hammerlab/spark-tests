name := "spark-tests"
v"2.3.1"

dep(
  paths % "1.5.0",
  scalatest,
  spark,
  spark_util % "2.0.4",
  testUtils
)

// Don't include default parent-plugin test-deps
clearTestDeps
scala211Only
