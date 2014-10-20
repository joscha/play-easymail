import play.PlayJava

name := "play-easymail-usage"

scalaVersion := "2.11.1"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "com.feth" %% "play-easymail" % "0.6.5-SNAPSHOT",
  javaCore
)

resolvers ++= Seq(
  "play-easymail (release)" at "http://joscha.github.io/play-easymail/repo/releases/",
  "play-easymail (snapshot)" at "http://joscha.github.io/play-easymail/repo/snapshots/"
)

lazy val root = (project in file(".")).enablePlugins(PlayJava)
