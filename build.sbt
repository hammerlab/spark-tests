name := "spark-tests"

version := "2.1.0-SNAPSHOT"

deps ++= Seq(
  paths % "1.1.1-SNAPSHOT",
  scalatest,
  spark,
  spark_util % "1.2.0-SNAPSHOT",
  testUtils
)

testUtilsVersion := "1.2.4-SNAPSHOT"

// Don't include default parent-plugin test-deps
testDeps := Nil
