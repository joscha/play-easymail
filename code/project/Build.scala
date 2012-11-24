import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "play-easymail"
    val libVersion      = "0.1-SNAPSHOT"

    val appDependencies = Seq(
        javaCore,
        javaJdbc,
        javaEbean
    )

    val main = play.Project(appName, libVersion, appDependencies).settings(
      	version := libVersion,
     	  organization := "com.feth",
     	  resolvers += "Daniel's Repository" at "http://danieldietrich.net/repository/snapshots/",
      	libraryDependencies += "com.typesafe" % "play-plugins-mailer_2.10" % "2.1-SNAPSHOT",
        publishArtifact in packageDoc := false
    )
}
