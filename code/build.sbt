organization := "com.feth"

name := "play-easymail"

scalaVersion := "2.11.1"

version := "0.6.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "com.typesafe" %% "play-plugins-mailer" % "2.3.0"
)

resolvers ++= Seq(
  // this is only for play-plugins-mailer 2.3.0 as long as it is not yet published to the typesquare repo
  Resolver.url("play-easymail (release)", url("http://joscha.github.io/play-easymail/repo/releases/"))(Resolver.ivyStylePatterns)
)

publishArtifact in packageDoc := false

lazy val root = (project in file(".")).enablePlugins(PlayJava)
