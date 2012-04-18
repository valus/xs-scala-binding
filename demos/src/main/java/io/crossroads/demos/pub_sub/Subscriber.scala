package io.crossroads.demos.pub_sub

import io.crossroads.XS

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
	    System.out.println("Recv msg: " + new String(sub.recvmsg(0)))
	  }
    }
}