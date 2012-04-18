import Dependencies._

organization          := "io.crossroads"

version               := "1.0.0-SNAPSHOT"

homepage              := Some(url("https://github.com/valus/xs-scala-binding"))

licenses              += ("GNU LESSER GENERAL PUBLIC LICENSE Version 3", url("http://www.gnu.org/copyleft/lesser.html"))

startYear             := Some(2012)

organizationName      := "xs-scala-binding"

crossScalaVersions    := Seq("2.9.2", "2.9.1-1", "2.9.1")

// Settings for Sonatype compliance
pomIncludeRepository  := { _ => false }

publishTo 			  <<= version { v: String =>
  									val nexus = "https://oss.sonatype.org/"
  									if (v.trim.endsWith("SNAPSHOT"))
    									Some("snapshots" at nexus + "content/repositories/snapshots")
  									else
    									Some("releases" at nexus + "service/local/staging/deploy/maven2")
					  }

pomExtra              ~= (_ ++ {ScmInfo.toXml})

pomExtra              ~= (_ ++ {Developers.toXml})