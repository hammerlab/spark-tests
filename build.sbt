name := "spark-tests"

version := "2.3.0-SNAPSHOT"

deps ++= Seq(
  paths % "1.3.1",
  scalatest,
  spark,
  spark_util % "2.0.0-SNAPSHOT",
  testUtils
)

// Don't include default parent-plugin test-deps
testDeps := Nil
