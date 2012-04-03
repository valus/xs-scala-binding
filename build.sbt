organization := "io.crossroads"

name := "xs-scala-binding"

version := "1.0.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "net.java.dev.jna" % "jna" % "3.4.0",
  "com.github.jnr" % "jnr-constants" % "0.8.2",
  "org.scalatest" %% "scalatest" % "1.6.1" % "test"
)

scalacOptions := Seq("-deprecation", "-unchecked")

publishTo <<= version { v: String =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { x => false }

pomExtra := (
  <url>https://github.com/valus/xs-scala-binding</url>
  <licenses>
    <license>
      <name>Apache License</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>https://git@github.com/valus/xs-scala-binding.git</url>
    <connection>scm:https://git@github.com/valus/xs-scala-binding.git</connection>
  </scm>
  <developers>
    <developer>
      <id>valus</id>
      <name>Marcin Stelmach</name>
      <url>www.stelmach.biz</url>
    </developer>
  </developers>
)
