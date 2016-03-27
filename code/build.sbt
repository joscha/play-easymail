organization := "com.feth"

name := "play-easymail"

scalaVersion := "2.11.7"

crossScalaVersions := Seq("2.11.7")

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-mailer" % "5.0.0-M1"
)

lazy val root = (project in file(".")).enablePlugins(PlayJava)
