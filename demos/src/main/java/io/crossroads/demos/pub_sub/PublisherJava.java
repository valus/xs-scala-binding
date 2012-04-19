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
package io.crossroads.demos.pub_sub;

import io.crossroads.XS;
import io.crossroads.XS.Context;
import io.crossroads.XS.Socket;

public class PublisherJava {

	private static final String ENDPOINT = "tcp://127.0.0.1:5533";
		
	public static void main(String[] args) {
		
		final Context context = XS.context();
		final Socket pub = context.socket(XS.PUB);
		pub.bind(ENDPOINT);
	  
		final String msg = "hello";
	  
		while(true) {
			System.out.println("Send msg: " + msg);
			pub.sendmsg(msg.getBytes(), 0);
		}
    } 
}