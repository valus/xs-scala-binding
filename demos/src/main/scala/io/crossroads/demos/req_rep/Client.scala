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