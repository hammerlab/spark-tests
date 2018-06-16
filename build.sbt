name := "spark-tests"
v"2.3.3"

dep(
  paths % "1.5.0",
  scalatest,
  spark,
  spark_util % "3.0.0",
  hammerlab.test.base
)

// Don't include default parent-plugin test-deps
clearTestDeps
`2.11` only
