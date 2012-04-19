
import sbt._

object PluginDef extends Build {
  lazy val root              = Project("plugins", file(".")) dependsOn(gpgPlugin)
  lazy val gpgPlugin       	 = uri("git://github.com/sbt/xsbt-gpg-plugin.git")
}