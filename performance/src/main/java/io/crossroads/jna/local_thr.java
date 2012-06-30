package io.crossroads.jna;

import java.nio.ByteBuffer;
import io.crossroads.jna.*;

import com.sun.jna.Pointer;

public class local_thr {

	private static final CrossroadsIOLibrary xs = CrossroadsIO$.MODULE$.loadLibrary();
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
        Pointer ctx = null;
        Pointer sock = null;
        int rc;
        int i;

        Pointer watch = null;
        long elapsed = 0;
        long throughput = 0;
        double megabits = 0.0;

        bind_to = "tcp://127.0.0.1:3000";
        message_size = 1;
        message_count = 1000000;
        System.out.printf("args: %s | %d | %d\n",
                          bind_to, message_size, message_count);

        ctx = xs.xs_init();
        if (ctx == null) {
            System.out.printf("error in xs_init: %s\n",
                              xs.xs_strerror(xs.xs_errno()));
            return;
        }
        System.out.printf("XS inited\n");

        sock = xs.xs_socket(ctx, XS.PULL);
        if (sock == null) {
            System.out.printf("error in xs_socket: %s\n",
                              xs.xs_strerror(xs.xs_errno()));
            return;
        }
        System.out.printf("XS PULL socket created\n");

        //  Add your socket options here.

        rc = xs.xs_bind(sock, bind_to);
        if (rc == -1) {
            System.out.printf("error in xs_bind: %s\n",
                              xs.xs_strerror(xs.xs_errno()));
            return;
        }
        System.out.printf("XS PULL socket bound to %s\n", bind_to);

        int size = 128;
        ByteBuffer bb = ByteBuffer.allocate(size);
        byte[] bba = bb.array();

        rc = xs.xs_recv(sock, bba, size, 0);
        if (rc < 0) {
            System.out.printf("error in xs_recv: %s\n",
                              xs.xs_strerror(xs.xs_errno()));
            return;
        }
        if (rc != message_size) {
            System.out.printf("message of incorrect size received\n");
            return;
        }

        watch = xs.xs_stopwatch_start();

        System.out.printf("XS running %d iterations...\n", message_count - 1);
        for (i = 0; i != message_count - 1; i++) {
            rc = xs.xs_recv(sock, bba, size, 0);
            if (rc < 0) {
                System.out.printf("error in xs_recv: %s\n",
                                  xs.xs_strerror(xs.xs_errno()));
                return;
            }
            if (rc != message_size) {
                System.out.printf("message of incorrect size received\n");
                return;
            }
        }

        elapsed = xs.xs_stopwatch_stop(watch).longValue();
        if (elapsed == 0)
            elapsed = 1;

        throughput = (long) ((double) message_count / (double) elapsed * 1000000);
        megabits = (double) (throughput * message_size * 8) / 1000000;

        System.out.printf("message size: %d [B]\n", message_size);
        System.out.printf("message count: %d\n", message_count);
        System.out.printf("mean throughput: %d [msg/s]\n", (int) throughput);
        System.out.printf("mean throughput: %.3f [Mb/s]\n", megabits);

        rc = xs.xs_close(sock);
        if (rc != 0) {
            System.out.printf("error in xs_close: %s\n",
                              xs.xs_strerror(xs.xs_errno()));
            return;
        }

        rc = xs.xs_term(ctx);
        if (rc != 0) {
            System.out.printf("error in xs_term: %s\n",
                              xs.xs_strerror(xs.xs_errno()));
            return;
        }
        System.out.printf("XS done running\n");
	}
}
