import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "play-easymail-usage"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
    	javaCore,
      	"com.feth" %% "play-easymail" % "0.2-SNAPSHOT"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      resolvers += Resolver.url("play-easymail (release)", url("http://joscha.github.com/play-easymail/repo/releases/"))(Resolver.ivyStylePatterns),
      resolvers += Resolver.url("play-easymail (snapshot)", url("http://joscha.github.com/play-easymail/repo/snapshots/"))(Resolver.ivyStylePatterns)
    )

}
