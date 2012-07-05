package io.crossroads.jni;

import java.nio.ByteBuffer;

public class remote_thr {

	
	public static void main(String[] args) {
		new remote_thr();
	}

	public remote_thr() {

        String connect_to;
        int message_count;
        int message_size;
        long ctx = -1;
        long sock = -1;
        int rc;
        int i;

        connect_to = "tcp://127.0.0.1:3000";
        message_size = 1;
        message_count = 10000000;
        System.out.printf("args: %s | %d | %d\n",
                          connect_to, message_size, message_count);
        
        ctx = XSLibrary.xs_init();
        if (ctx < 0) {
            System.out.printf("error in xs_init: %s\n",
            		XSLibrary.xs_strerror(XSLibrary.xs_errno()));
            return;
        }
        System.out.printf("XS inited\n");

        sock = XSLibrary.xs_socket(ctx, XS.PUSH());
        if (sock < 0) {
            System.out.printf("error in xs_socket: %s\n",
            		XSLibrary.xs_strerror(XSLibrary.xs_errno()));
            return;
        }
        System.out.printf("XS PUSH socket created\n");

        //  Add your socket options here.

        rc = XSLibrary.xs_connect(sock, connect_to);
        if (rc < 0) {
            System.out.printf("error in xs_connect: %s\n",
            		XSLibrary.xs_strerror(XSLibrary.xs_errno()));
            return;
        }
        System.out.printf("XS PUSH socket connected to %s\n", connect_to);

        int size = 128;
        ByteBuffer bb = ByteBuffer.allocate(size);
        byte[] bba = bb.array();

        System.out.printf("XS running %d iterations...\n", message_count);
        for (i = 0; i != message_count; i++) {
            rc = XSLibrary.xs_send(sock, bba, message_size, 0);
            if (rc < 0) {
                System.out.printf("error in xs_send: %s\n",
                		XSLibrary.xs_strerror(XSLibrary.xs_errno()));
                return;
            }
            if (rc != message_size) {
                System.out.printf("message of incorrect size sent\n");
                return;
            }
        }

        rc = XSLibrary.xs_close(sock);
        if (rc != 0) {
            System.out.printf("error in xs_close: %s\n",
            		XSLibrary.xs_strerror(XSLibrary.xs_errno()));
            return;
        }

        rc = XSLibrary.xs_term(ctx);
        if (rc != 0) {
            System.out.printf("error in xs_term: %s\n",
            		XSLibrary.xs_strerror(XSLibrary.xs_errno()));
            return;
        }
        System.out.printf("XS done running\n");
    }
}
