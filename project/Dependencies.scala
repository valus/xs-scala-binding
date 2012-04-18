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


object Dependencies {

  type ModuleMap = String => ModuleID

  lazy val slf4jVersion = "1.6.4"

  // Compile scope:
  lazy val jna          = "net.java.dev.jna"	% "jna"      		% "3.4.0"
  lazy val jnr			= "com.github.jnr"      % "jnr-constants" 	% "0.8.2"
  lazy val scalatest    = "org.scalatest"       %% "scalatest" 		% "1.6.1"	% "test"


  // Provided scope:
  lazy val logback         = "ch.qos.logback"    % "logback-classic" % "1.0.1"      % "provided"
  lazy val log4j           = "log4j"             % "log4j"           % "1.2.16"     % "provided"
  lazy val slf4j_log4j12   = "org.slf4j"         % "slf4j-log4j12"   % slf4jVersion % "provided"
  lazy val slf4j_api       = "org.slf4j"         % "slf4j-api"       % slf4jVersion	% "provided"
  
  //libraryDependencies ++= Seq(jna, jnr, scalatest, logback, log4j, slf4j_log4j12, slf4j_api)
}