organization := "com.feth"

name := "play-easymail"

scalaVersion := "2.11.6"

crossScalaVersions := Seq("2.10.5", "2.11.6")

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-mailer" % "3.0.1"
)

lazy val root = (project in file(".")).enablePlugins(PlayJava)
