name := "spark-tests"
v"2.4.0"
+`2.11`
dep(
  paths % "1.5.0",
  scalatest,
  slf4j,

  // compile-scoped spark dep brings in guava 16 (transitively via apache curator)
  spark - guava - log4j,
  guava % "14.0.1" provided,

  spark_util % "3.1.0",
  hammerlab.test.base,
  test_logging,
)

// Don't include default parent-plugin test-deps
clearTestDeps
