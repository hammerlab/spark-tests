name := "spark-tests"

version := "1.1.2"

providedDeps += libraries.value('spark)

libraryDependencies ++= Seq(
  libraries.value('spark_testing_base),
  "org.hammerlab" %% "spark-util" % "1.1.1",
  libraries.value('scalatest)
)
