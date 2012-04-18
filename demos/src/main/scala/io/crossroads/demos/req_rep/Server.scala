package io.crossroads.demos.req_rep

import io.crossroads.XS
import io.crossroads.XS.Socket

object Server {

	val endpoint = "tcp://127.0.0.1:5533"
		
	def main(args: Array[String]): Unit = {
		var rep: Socket = null
		try {
			val context = XS.context
			rep = context.socket(XS.REP)
			rep.bind(endpoint)
			
			val responseMessage = "hi..."
				
			while(true) {
			  val incomingMessage = rep.recvmsg(0)
			  System.out.println("Server recv: " + new String(incomingMessage))
			  rep.send(responseMessage.getBytes, responseMessage.length, 0)
			}
		} finally {
			rep.close
		}
	}
}