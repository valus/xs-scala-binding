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

import io.crossroads.jna.XS;
import io.crossroads.jna.XS.Context;
import io.crossroads.jna.XS.Poller;
import io.crossroads.jna.XS.Socket;

public class SubscriberJava {
	
	private static final String ENDPOINT = "tcp://127.0.0.1:5533";
	
	public static void main(String[] args) {
		
		final Context context = XS.context();
		final Socket sub = context.socket(XS.PULL);
	  
		sub.bind(ENDPOINT);
		int nrMsg = 0;
		long startTime = System.currentTimeMillis();
		while(true) {
			sub.recv(1,0);
			//System.out.println("Recv msg: " + new String(sub.recv(1,0)));
			if(nrMsg == 1000000) break;
			nrMsg++;
		}
		long endTime = System.currentTimeMillis();
		long time = (endTime - startTime)/1000;
		System.out.println("Time: " + time);
		System.out.println("Throughput: " + nrMsg/time);
    }
}