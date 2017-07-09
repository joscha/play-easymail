organization := "com.feth"

name := "play-easymail"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-mailer" % "6.0.0",
  guice
)

lazy val root = (project in file(".")).enablePlugins(PlayJava)

releasePublishArtifactsAction := PgpKeys.publishSigned.value
