import play.PlayJava

name := "play-easymail-usage"

scalaVersion := "2.11.2"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  // Comment the next line for local development:
  "com.feth" %% "play-easymail" % "0.6.5-SNAPSHOT",
  javaCore
)

resolvers ++= Seq(
  "play-easymail (release)" at "http://joscha.github.io/play-easymail/repo/releases/",
  "play-easymail (snapshot)" at "http://joscha.github.io/play-easymail/repo/snapshots/"
)

//  Uncomment the next line for local development of the Play Authenticate core:
//lazy val playEasymail = project.in(file("modules/play-easymail")).enablePlugins(PlayJava)

lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
/*  Uncomment the next lines for local development of the Play Authenticate core: */
//  .dependsOn(playEasymail)
//  .aggregate(playEasymail)
