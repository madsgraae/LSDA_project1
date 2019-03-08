name := "LSDA-Project1"

scalaVersion := "2.11.12"

scalacOptions ++= Seq("-deprecation")


// grading libraries
libraryDependencies ++= Seq(
  "junit" % "junit" % "4.10" % Test,
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)
