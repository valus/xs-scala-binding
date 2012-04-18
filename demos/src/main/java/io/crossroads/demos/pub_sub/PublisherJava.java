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