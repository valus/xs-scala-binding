organization := "io.crossroads"

name := "xs-scala-binding"

version := "1.0.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "net.java.dev.jna" % "jna" % "3.4.0",
  "com.github.jnr" % "jnr-constants" % "0.8.2",
  "org.scalatest" %% "scalatest" % "1.6.1" % "test"
)

scalacOptions := Seq("-deprecation", "-unchecked")

publishTo := Some(Resolver.file("GitHub Pages", file("../xs-scala-binding-pages/maven/")))

publishArtifact in (Compile, packageDoc) := false 
