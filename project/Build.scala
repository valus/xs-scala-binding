/*
    Copyright (c) 2012 the original author or authors.

    This file is part of scala-xs-binding project.

    scala-xs-binding is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation; either version 3 of the License, or
    (at your option) any later version.

    scala-xs-binding is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
import sbt._
import Keys._
import Dependencies._

object BuildDef extends Build {
  	
  lazy val root = Project("xs-scala-binding-parent", file("."))
		  .settings(description:= "Parent for Crossroads I/O Scala binding project", 
			publishArtifact := false) aggregate(core, demos)

  // Core Project  
  // ------------
  lazy val core = Project(id = "xs-scala-binding", base = file("core"))
    .settings(description:= "Crossroads I/O Scala binding", 
    		organization:= "io.crossroads", 
    		name:="xs-scala-binding",
    		organizationName:= "Scalaric", 
    		version := "1.0.0",
    		homepage := Some(url("https://github.com/valus/xs-scala-binding")),
    		licenses += ("GNU LESSER GENERAL PUBLIC LICENSE Version 3", url("http://www.gnu.org/copyleft/lesser.html")),
    		startYear := Some(2012),
    		libraryDependencies ++= Seq(jna, jnr, scalatest),
    		pomIncludeRepository  := { _ => false },
  			publishTo <<= version { v: String =>
  				val nexus = "https://oss.sonatype.org/"
  					if (v.trim.endsWith("SNAPSHOT"))
    					Some("snapshots" at nexus + "content/repositories/snapshots")
  					else
    					Some("releases" at nexus + "service/local/staging/deploy/maven2")
			},
			pomExtra ~= (_ ++ {ScmInfo.toXml}),
			pomExtra ~= (_ ++ {Developers.toXml})
    		)

  // Demos Project
  // ------------
  lazy val demos = Project(id = "xs-scala-binding-demos", base = file("demos")) dependsOn(core)
    .settings(description := "Examples for Scala binding", 
    		organization:= "io.crossroads", 
    		name:="xs-scala-binding-demos",
    		organizationName:= "Scalaric",
    		version := "1.0.0",
    		homepage := Some(url("https://github.com/valus/xs-scala-binding")),
    		licenses += ("GNU LESSER GENERAL PUBLIC LICENSE Version 3", url("http://www.gnu.org/copyleft/lesser.html")),
    		startYear := Some(2012),
			publishArtifact := false)
}