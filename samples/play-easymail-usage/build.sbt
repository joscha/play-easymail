name := "play-easymail-usage"

scalaVersion := "2.11.1"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "com.feth" %% "play-easymail" % "0.6.1-SNAPSHOT",
  javaCore
)

resolvers ++= Seq(
  Resolver.url("play-easymail (release)", url("http://joscha.github.io/play-easymail/repo/releases/"))(Resolver.ivyStylePatterns),
  Resolver.url("play-easymail (snapshot)", url("http://joscha.github.io/play-easymail/repo/snapshots/"))(Resolver.ivyStylePatterns)
)

lazy val root = (project in file(".")).enablePlugins(PlayJava)
