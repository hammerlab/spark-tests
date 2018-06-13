name := "spark-tests"
v"2.3.2"

dep(
  paths % "1.5.0",
  scalatest,
  spark,
  spark_util % "2.0.4",
  hammerlab.test.base
)

// Don't include default parent-plugin test-deps
clearTestDeps
`2.11` only
