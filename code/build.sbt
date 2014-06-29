organization := "com.feth"

name := "play-easymail"

scalaVersion := Option(System.getProperty("scala.version")).getOrElse("2.11.1")

version := "0.6.1-SNAPSHOT"

publishTo <<= (version) { version: String =>
  if (version.trim.endsWith("SNAPSHOT")) Some(Resolver.file("file",  new File( "../repo/snapshots" )))
  else                                   Some(Resolver.file("file",  new File( "../repo/releases" )))
}

libraryDependencies ++= Seq(
  "com.typesafe" %% "play-plugins-mailer" % "2.3.0"
)

resolvers ++= Seq(
  // this is only for play-plugins-mailer 2.3.0 as long as it is not yet published to the typesquare repo
  Resolver.url("play-easymail (release)", url("http://joscha.github.io/play-easymail/repo/releases/"))(Resolver.ivyStylePatterns)
)

publishArtifact in packageDoc := false

lazy val root = (project in file(".")).enablePlugins(PlayJava)
