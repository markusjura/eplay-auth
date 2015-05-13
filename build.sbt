name := "eplay-auth"

version := "1.0.1"

scalaVersion := "2.11.6"

crossScalaVersions := Seq("2.10.4", "2.11.6")

libraryDependencies ++= Seq(
  "org.scalatestplus" %% "play" % "1.2.0" % "test",
  "com.wordnik" %% "swagger-play2" % "1.3.12"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)