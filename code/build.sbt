import play.PlayJava

organization := "com.feth"

name := "play-easymail"

scalaVersion := "2.11.2"

crossScalaVersions := Seq("2.10.4", "2.11.2")

libraryDependencies ++= Seq(
  "com.typesafe.play.plugins" %% "play-plugins-mailer" % "2.3.1"
)

lazy val root = (project in file(".")).enablePlugins(PlayJava)
