import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "play-easymail"
    val libVersion      = "0.1-SNAPSHOT"

    val main = PlayProject(appName, libVersion).settings(
      	version := libVersion,
     	organization := "com.feth",
      	libraryDependencies += "com.typesafe" %% "play-plugins-mailer" % "2.0.4"
    )
}