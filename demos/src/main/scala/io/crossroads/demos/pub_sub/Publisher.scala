package io.crossroads.demos.pub_sub

import io.crossroads.XS

object Publisher {

	val endpoint = "tcp://127.0.0.1:5533"
		
	def main(args: Array[String]): Unit = {
      val context = XS.context
	  val pub = context.socket(XS.PUB)
	  pub.bind(endpoint)
	  
	  val msg = "hello"
	  
	  while(true) {
	  	System.out.println("Send msg: " + msg);
	  	pub.sendmsg(msg.getBytes, 0)
	  }
    } 
}