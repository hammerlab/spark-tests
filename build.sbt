name := "spark-tests"

version := "2.2.0"

deps ++= Seq(
  paths % "1.2.0",
  scalatest,
  spark,
  spark_util % "1.3.0",
  testUtils
)

// Don't include default parent-plugin test-deps
testDeps := Nil
