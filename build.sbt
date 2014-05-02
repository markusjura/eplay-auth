name := "ttt"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.1.0" % "test",
  "com.wordnik" %% "swagger-play2" % "1.3.5"
)

scalariformSettings

play.Project.playScalaSettings
