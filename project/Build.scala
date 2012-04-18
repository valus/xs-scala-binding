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

  lazy val XSScalaBinding =
    Project("xs-scala-binding", file("."))
      .aggregate(core, demos)

  
  // Core Project  
  // ------------
  lazy val core = Project(id = "xs-scala-binding-core", base = file("core"))
    .settings(description:= "Crossroads I/O Scala binding", 
    		libraryDependencies ++= Seq(jna, jnr, scalatest))

  // Demos Project
  // ------------
  lazy val demos = Project(id = "xs-scala-binding-demos", base = file("demos"))
    .settings(description := "Examples for Scala binding", 
    		libraryDependencies ++= Seq(slf4j_api, logback, slf4j_log4j12))
}