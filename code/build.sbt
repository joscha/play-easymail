import play.PlayJava

organization := "com.feth"

name := "play-easymail"

scalaVersion := "2.11.1"

crossScalaVersions := Seq("2.10.4", "2.11.1")

javacOptions ++= {
  if (System.getProperty("rt.path") != null)  Seq("-source", "1.6", "-target", "1.6", "-bootclasspath", System.getProperty("rt.path"))
  else                                        Seq()
}

version := "0.6.5-SNAPSHOT"

publishTo <<= (version) { version: String =>
  if (version.trim.endsWith("SNAPSHOT")) Some(Resolver.file("file",  new File( "../repo/snapshots" )))
  else                                   Some(Resolver.file("file",  new File( "../repo/releases" )))
}

libraryDependencies ++= Seq(
  "com.typesafe.play.plugins" %% "play-plugins-mailer" % "2.3.1"
)

publishArtifact in packageDoc := false

lazy val root = (project in file(".")).enablePlugins(PlayJava)
