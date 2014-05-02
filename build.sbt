name := "eplay-auth"

version := "1.0"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.1.0" % "test",
  "com.wordnik" %% "swagger-play2" % "1.3.5",
  "com.newrelic.agent.java" % "newrelic-agent" % "2.21.5"
)

scalariformSettings

play.Project.playScalaSettings
