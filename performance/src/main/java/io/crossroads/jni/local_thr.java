package io.crossroads.jni;

import java.nio.ByteBuffer;

public class local_thr {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new local_thr();
	}

	public local_thr() {
		String bind_to;
        int message_count;
        int message_size;
        long ctx = -1;
        long sock = -1;
        int rc;
        int i;

        long watch = -1;
        long elapsed = 0;
        long throughput = 0;
        double megabits = 0.0;

        bind_to = "tcp://127.0.0.1:3000";
        message_size = 1;
        message_count = 1000000;
        System.out.printf("args: %s | %d | %d\n",
                          bind_to, message_size, message_count);

        ctx = XSLibrary.xs_init();
        if (ctx < 0) {
            System.out.printf("error in xs_init: %s\n",
            		XSLibrary.xs_strerror(XSLibrary.xs_errno()));
            return;
        }
        System.out.printf("XS inited\n");

        sock = XSLibrary.xs_socket(ctx, XS.PULL());
        if (sock < 0) {
            System.out.printf("error in xs_socket: %s\n",
            		XSLibrary.xs_strerror(XSLibrary.xs_errno()));
            return;
        }
        System.out.printf("XS PULL socket created\n");

        //  Add your socket options here.

        rc = XSLibrary.xs_bind(sock, bind_to);
        if (rc < 0) {
            System.out.printf("error in xs_bind: %s\n",
            		XSLibrary.xs_strerror(XSLibrary.xs_errno()));
            return;
        }
        System.out.printf("XS PULL socket bound to %s\n", bind_to);

        int size = 128;
        ByteBuffer bb = ByteBuffer.allocate(size);
        byte[] bba = bb.array();

        bba = XSLibrary.xs_recv(sock, bba, message_size, 0);
        /*if (rc < 0) {
            System.out.printf("error in xs_recv: %s\n",
            		XSLibrary.xs_strerror(XSLibrary.xs_errno()));
            return;
        }
        if (rc != message_size) {
            System.out.printf("message of incorrect size received\n");
            return;
        }*/

        watch = XSLibrary.xs_stopwatch_start();

        System.out.printf("XS running %d iterations...\n", message_count - 1);
        for (i = 0; i != message_count - 1; i++) {
            bba = XSLibrary.xs_recv(sock, bba, message_size, 0);
            /*if (rc < 0) {
                System.out.printf("error in xs_recv: %s\n",
                		XSLibrary.xs_strerror(XSLibrary.xs_errno()));
                return;
            }
            if (rc != message_size) {
                System.out.printf("message of incorrect size received\n");
                return;
            }*/
        }

        elapsed = XSLibrary.xs_stopwatch_stop(watch);
        if (elapsed == 0)
            elapsed = 1;

        throughput = (long) ((double) message_count / (double) elapsed * 1000000);
        megabits = (double) (throughput * message_size * 8) / 1000000;

        System.out.printf("message size: %d [B]\n", message_size);
        System.out.printf("message count: %d\n", message_count);
        System.out.printf("mean throughput: %d [msg/s]\n", (int) throughput);
        System.out.printf("mean throughput: %.3f [Mb/s]\n", megabits);

        rc = XSLibrary.xs_close(sock);
        if (rc < 0) {
            System.out.printf("error in xs_close: %s\n",
            		XSLibrary.xs_strerror(XSLibrary.xs_errno()));
            return;
        }

        rc = XSLibrary.xs_term(ctx);
        if (rc < 0) {
            System.out.printf("error in xs_term: %s\n",
            		XSLibrary.xs_strerror(XSLibrary.xs_errno()));
            return;
        }
        System.out.printf("XS done running\n");
	}
}
