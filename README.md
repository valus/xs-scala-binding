Scala Binding for Crossroads I/O
================================
<i>(Fork of Scala binding for 0MQ; [zeromq-scala-binding][zeromq-scala])</i>

The Scala binding for [Crossroads I/O][xs] is based on libxs versions 1.2.0 and uses [JNA][jna] and [JNI][jni]. 
The Scala binding is a thin wrapper of the Crossroads I/O API.

#### NOTE:
* Binding based on JNA is fully supports all functionalities of libxs 1.2.0.
* Binding based on JNI is under development and it doesn't have all functionalities of libx 1.2.0 implemented.

## Installation and usage.

### SBT

For SBT projects add below code to the `project/build.sbt`, for plugin library dependencies with the following lines:

    resolvers += "Sonatype Repository " at "https://oss.sonatype.org/content/repositories/snapshots/"
  
    libraryDependencies += "io.crossroads" %% "xs-scala-binding" % "1.0.3-SNAPSHOT"


### Maven

For maven projects add below code to the `pom.xml` file:

	<dependencies>
		<dependency>
	   		<groupId>io.crossroads</groupId>
			<artifactId>xs-scala-binding</artifactId>
			<version>1.0.3-SNAPSHOT</version>
		</dependency>
	</dependencies>

	<repositories>
		<repository>
			<id>sonatype</id>
			<url>https://oss.sonatype.org/content/repositories/snapshots/</url>
		</repository>
	</repositories>
	

## Example of usage

More examples you can find in `demos` subproject.

### PUB-SUB pattern

#### Scala:

##### Publisher:

```scala
import io.crossroads.jna.XS

object Publisher {

	val endpoint = "tcp://127.0.0.1:5533"
		
	def main(args: Array[String]): Unit = {
      val context = XS.context
	  val pub = context.socket(XS.PUB)
	  pub.bind(endpoint)
	  
	  val msg = "hello"
	  
	  while(true) {
	  	System.out.println("Send msg: " + msg);
	  	pub.send(msg.getBytes, 5, 0)
	  }
    } 
}
```

##### Subscriber:
```scala
import io.crossroads.jna.XS

object Subscriber {
	
	val endpoint = "tcp://127.0.0.1:5533"
		
	def main(args: Array[String]): Unit = {
      val context = XS.context
	  val (sub, poller) = (
	  		context.socket(XS.SUB),
	  		context.poller
	  )
	  
	  sub.connect(endpoint)
	  sub.subscribe(Array.empty)
	  poller.register(sub)
		  
	  while(true) {
	    System.out.println("Recv msg: " + new String(sub.recv(5,0)))
	  }
    }
}
```	

#### Java:

##### Publisher:

```java
import io.crossroads.jna.XS;
import io.crossroads.jna.XS.Context;
import io.crossroads.jna.XS.Socket;

public class Publisher {

	private static final String ENDPOINT = "tcp://127.0.0.1:5533";
		
	public static void main(String[] args) {
		
		final Context context = XS.context();
		final Socket pub = context.socket(XS.PUB);
		pub.bind(ENDPOINT);
	  
		final String msg = "hello";
	  
		while(true) {
			System.out.println("Send msg: " + msg);
			pub.send(msg.getBytes(), 5, 0);
		}
    } 
}
```

##### Subscriber:
```java
import io.crossroads.jna.XS;
import io.crossroads.jna.XS.Context;
import io.crossroads.jna.XS.Poller;
import io.crossroads.jna.XS.Socket;

public class SubscriberJava {
	
	private static final String ENDPOINT = "tcp://127.0.0.1:5533";
	
	public static void main(String[] args) {
		
		final Context context = XS.context();
		final Socket sub = context.socket(XS.SUB);
		final Poller poller = context.poller();
	  
		sub.connect(ENDPOINT);
		sub.subscribe(new byte[]{});
		poller.register(sub);
		  
		while(true) {
			System.out.println("Recv msg: " + new String(sub.recv(5, 0)));
		}
    }
}
```


## Issues

Issues should be logged on the [GitHub issue tracker][issues] for this project.

When reporting issues, please include the following information if possible:

* Version of xs-scala-binding
* Version of libxs being used
* Code snippet demonstrating the failure
* Runtime environment 

## Performance

Tests run on machine with Intel Core i7 2.2GHz and 8GB RAM.
How To run a test:

* Run `xs-scala-binding.sh` script:

```bash
./xs-scala-binding.sh
```

* Go to the sxs-perf (performance) submodule:

```sbt
project sxs-perf
```
* Run a test:

```sbt
run
```

### JNA Performance

<pre>
[info] Running io.crossroads.jna.local_thr 
args: tcp://127.0.0.1:3000 | 1 | 10000000
XS inited
XS PULL socket created
XS PULL socket bound to tcp://127.0.0.1:3000
XS running 999999 iterations...
message size: 1 [B]
message count: 1000000
mean throughput: 292541 [msg/s]
mean throughput: 2.340 [Mb/s]
</pre>

### JNI Performance

<pre>
[info] Running io.crossroads.jni.local_thr 
args: tcp://127.0.0.1:3000 | 1 | 10000000
XS inited
XS PULL socket created
XS PULL socket bound to tcp://127.0.0.1:3000
XS running 9999999 iterations...
message size: 1 [B]
message count: 10000000
mean throughput: 4459314 [msg/s]
mean throughput: 35.675 [Mb/s]
</pre>


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
[jna]: http://en.wikipedia.org/wiki/Java_Native_Access
[jni]: http://en.wikipedia.org/wiki/Java_Native_Interface