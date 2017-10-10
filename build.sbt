name := "spark-tests"

version := "2.3.0"

deps ++= Seq(
  paths % "1.3.1",
  scalatest,
  spark,
  spark_util % "2.0.0",
  testUtils
)

// Don't include default parent-plugin test-deps
testDeps := Nil
