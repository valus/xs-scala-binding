Scala Binding for Crossroads I/O
================================
<i>(Fork of Scala binding for 0MQ; [zeromq-scala-binding][zeromq-scala])</i>

The Scala binding for [Crossroads I/O][xs] is based on libxs versions 1.0.x and uses JNA for accessing native functions. 
The Scala binding is a thin wrapper of the Crossroads I/O API.


## Installation and usage.

### SBT

For SBT projects add below code to the `project/build.sbt`, for plugin library dependencies with the following lines:

    resolvers += "Sonatype Repository (snaphots)" at "https://oss.sonatype.org/content/repositories/snapshots"
  
    libraryDependencies += "io.crossroads" %% "xs-scala-binding" % "1.0.0-SNAPSHOT"


### Maven

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
	

## Example of usage

### PUB-SUB pattern

#### Publisher:
```scala
val context = XS.context
val pub = context.socket(XS.PUB)
pub.bind("tcp://127.0.0.1:3001")
		
var pubMsg = "test publish msg"
while(true) {
	pub.sendmsg(pubMsg.getBytes, 0)
}
```
#### Subscriber:
```scala
val context = XS.context
val sub = context.socket(XS.SUB)
sub.connect("tcp://127.0.0.1:3001")
sub.subscribe(Array.empty)
while(true) {
	val recvMsg = sub.recvmsg(0)
	System.out.println("Sub recv=" + new String(recvMsg))
}
```	

## Issues

Issues should be logged on the [GitHub issue tracker][issues] for this project.

When reporting issues, please include the following information if possible:

* Version of xs-scala-binding
* Version of libxs being used
* Code snippet demonstrating the failure
* Runtime environment 


## Contributing

To speed up the merge process, please follow the guidelines below when making a pull request:

* Create a new branch in your fork for the changes you intend to make. Working directly in master can often lead to unintended additions to the pull request later on.
* When appropriate, add to the Specifications project to cover any new functionality or defect fixes.
* Ensure all previous tests continue to pass

Pull requests will still be accepted if some of these guidelines are not followed: changes will just take longer to merge, as the missing pieces will need to be filled in.

## License

This project is released under the [LGPL][lgpl] license, as is the native libxs library. See LICENSE for more details as well as the [Crossroads I/O Licensing][xs-license] page.

[zeromq-scala]: https://github.com/kro/zeromq-scala-binding
[xs]: http://www.crossroads.io
[libxs]: https://github.com/crossroads-io/libxs
[xs-dl]: http://www.crossroads.io/download
[xs-license]: http://www.crossroads.io/dev:legal
[issues]: https://github.com/valus/xs-scala-binding/issues
[lgpl]: http://www.gnu.org/licenses/lgpl.html