package io.crossroads.demos.pub_sub;

import io.crossroads.XS;
import io.crossroads.XS.Context;
import io.crossroads.XS.Poller;
import io.crossroads.XS.Socket;

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
			System.out.println("Recv msg: " + new String(sub.recvmsg(0)));
		}
    }
}