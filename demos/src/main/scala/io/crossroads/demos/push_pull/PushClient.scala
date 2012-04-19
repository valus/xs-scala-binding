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
package io.crossroads.demos.push_pull

import io.crossroads.XS
import io.crossroads.XS.Socket

object PushClient {

	val endpoint = "tcp://127.0.0.1:3000";
	
	def main(args: Array[String]): Unit = {
		var push: Socket = null;
		try {
			val context = XS.context
			push = context.socket(XS.PUSH)
			push.connect(endpoint)
			
			val msg = "hello"
				
			while(true) {
				System.out.println("PushClient sending msg: " + msg)
				push.sendmsg(msg.getBytes, 0)
			}
		} finally {
			push.close
		}
	}
}