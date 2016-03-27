name := "play-easymail-usage"

scalaVersion := "2.11.7"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  // Comment the next line for local development:
  "com.feth" %% "play-easymail" % "0.8.0-SNAPSHOT"
)
//  Uncomment the next line for local development of the Play Easymail core:
//lazy val playEasymail = project.in(file("modules/play-easymail")).enablePlugins(PlayJava)

lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
/*  Uncomment the next lines for local development of the Play easymail core: */
//  .dependsOn(playEasymail)
//  .aggregate(playEasymail)
