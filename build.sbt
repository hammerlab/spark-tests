name := "spark-tests"

version := "1.1.0-SNAPSHOT"

libraryDependencies <++= libraries { v => Seq(
  v('spark),
  v('spark_testing_base),
  v('scalatest),
  "org.hammerlab" %% "spark-util" % "1.0.0"
)}
