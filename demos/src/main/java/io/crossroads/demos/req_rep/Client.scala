package io.crossroads.demos.req_rep

import io.crossroads.XS

object Client {

	val endpoint = "tcp://127.0.0.1:5533"
		
	def main(args: Array[String]): Unit = {
		val context = XS.context
		val req = context.socket(XS.REQ)
		req.connect(endpoint)
		
		val msg = "hello..."
		var i = 0
		
		while(i < 10) {
			System.out.println("Sending msg to server: " + msg)
			req.send(msg.getBytes, msg.length, 0)
			val endMessage = req.recvmsg(0)
			System.out.println("Receiving msg from server: " + new String(endMessage))
			i = i+1
		}
		req.close
	}

}