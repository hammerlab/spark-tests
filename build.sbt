name := "spark-tests"

version := "2.1.0"

deps ++= Seq(
  paths % "1.2.0",
  scalatest,
  spark,
  spark_util % "1.2.1",
  testUtils
)

testUtilsVersion := "1.3.0"

// Don't include default parent-plugin test-deps
testDeps := Nil
