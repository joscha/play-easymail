organization := "com.feth"

name := "play-easymail"

scalaVersion := "2.11.1"

version := "0.6.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "com.typesafe" % "play-plugins-mailer_2.11" % "2.3.0"
)

publishArtifact in packageDoc := false

lazy val root = (project in file(".")).enablePlugins(PlayJava)
