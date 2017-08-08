name := "spark-tests"

version := "2.1.2-SNAPSHOT"

deps ++= Seq(
  paths % "1.2.0",
  scalatest,
  spark,
  spark_util % "1.2.1",
  testUtils
)

// Don't include default parent-plugin test-deps
testDeps := Nil
