import Dependencies._

organization          := "io.crossroads"

name				  := "xs-scala-binding"

version               := "1.0.3-SNAPSHOT"

homepage              := Some(url("https://github.com/valus/xs-scala-binding"))

licenses              += ("GNU LESSER GENERAL PUBLIC LICENSE Version 3", url("http://www.gnu.org/copyleft/lesser.html"))

startYear             := Some(2012)

organizationName      := "Scalaric"

crossPaths := false

// call make -f Makefile.native clean
clean <<= (clean, resourceManaged in Compile, sourceDirectory, classDirectory in Compile,
      managedClasspath in Compile) map { (clean, dir, src, classDir, runPath) => {
    val home = System.getProperty("java.home")
    val basePath = runPath.map(_.data.toString).reduceLeft(_ + ":" + _)
    val classpath = classDir.toString + ":" + basePath
    val result = sbt.Process(
      "make" :: "-f" :: "Makefile" :: "clean" :: Nil,
      None,
      "COMPILE_PATH" -> classDir.toString,
      "CLASSPATH" -> classpath,
      "JAVA_HOME" -> home
      ) ! ;
    //
    if (result != 0)
      error("Error cleaning native library")
    clean
  }
}

// call make -f Makefile.native all
compile <<= (compile in Compile, resourceManaged in Compile, sourceDirectory, classDirectory in Compile,
      managedClasspath in Compile) map { (compile, dir, src, classDir, runPath) => {
    val superCompile = compile
    val home = System.getProperty("java.home")
    val basePath = runPath.map(_.data.toString).reduceLeft(_ + ":" + _)
    val classpath = classDir.toString + ":" + basePath
    val result = sbt.Process(
      "make" :: "-f" :: "Makefile" :: "all" :: Nil,
      None,
      "COMPILE_PATH" -> classDir.toString,
      "CLASSPATH" -> classpath,
      "JAVA_HOME" -> home
      ) ! ;
    //
    if (result != 0)
      error("Error compiling native library")
    superCompile
  }
}

fork in run := true

javaOptions in run += "-Djava.library.path=./target/so"