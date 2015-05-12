name := "eplay-auth"

version := "1.0.0"

scalaVersion := "2.11.6"

crossScalaVersions := Seq("2.10.4", "2.11.6")

resolvers += Resolver.bintrayRepo("markusjura", "maven")

libraryDependencies ++= Seq(
  "org.scalatestplus" %% "play" % "1.2.0" % "test",
  "com.markusjura" %% "swagger-play2" % "1.3.7"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)