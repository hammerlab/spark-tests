name := "spark-tests"
r"2.3.0"

dep(
  paths % "1.3.1",
  scalatest,
  spark,
  spark_util % "2.0.0",
  testUtils
)

// Don't include default parent-plugin test-deps
testDeps := Nil
