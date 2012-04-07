Scala Binding for Crossroads I/O
================================
<i>(Fork of Scala binding for 0MQ; <a href="https://github.com/kro/zeromq-scala-binding">zeromq-scala-binding</a>)</i>

The Scala binding for Crossroads I/O is based on libxs versions 1.0.x and uses JNA for accessing native functions. 
The Scala binding is a thin wrapper of the Crossroads I/O API.


Installation and usage.
----------------------

SBT
---

For SBT projects add below code to the `project/build.sbt`, for plugin library dependencies with the following lines:

    resolvers += "Sonatype Repository (snaphots)" at "https://oss.sonatype.org/content/repositories/snapshots"
  
    libraryDependencies += "io.crossroads" %% "xs-scala-binding" % "1.0.0-SNAPSHOT"


Maven
---

For maven projects add below code to the `pom.xml` file:

	<dependencies>
		<dependency>
	   		<groupId>io.crossroads</groupId>
			<artifactId>xs-scala-binding_2.9.1</artifactId>
			<version>1.0.0-SNAPSHOT</version>
		</dependency>
	</dependencies>

	<repositories>
		<repository>
			<id>sonatype</id>
			<url>https://oss.sonatype.org/content/repositories/snapshots</url>
		</repository>
	</repositories>
	

Example of usage
-----------------

PUB-SUB pattern

Publisher:

	val context = XS.context
	val pub = context.socket(XS.PUB)
	pub.bind("tcp://127.0.0.1:3001")
		
	var pubMsg = "test publish msg"
	while(true) {
		pub.sendmsg(pubMsg.getBytes, 0)
	}

Subscriber:

	val context = XS.context
	val sub = context.socket(XS.SUB)
	sub.connect("tcp://127.0.0.1:3001")
	sub.subscribe(Array.empty)
	while(true) {
		val recvMsg = sub.recvmsg(0)
		System.out.println("Sub recv=" + new String(recvMsg))
	}