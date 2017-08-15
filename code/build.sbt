organization := "com.feth"

name := "play-easymail"

scalaVersion := "2.11.11"
crossScalaVersions := Seq("2.11.11", "2.12.2")

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-mailer" % "6.0.1",
  "com.typesafe.play" %% "play-mailer-guice" % "6.0.1",
  guice
)

lazy val root = (project in file(".")).enablePlugins(PlayJava)

releasePublishArtifactsAction := PgpKeys.publishSigned.value
