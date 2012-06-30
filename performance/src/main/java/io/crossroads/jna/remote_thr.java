package io.crossroads.jna;

import java.nio.ByteBuffer;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

public class remote_thr {

	private static final CrossroadsIOLibrary xs = CrossroadsIO$.MODULE$.loadLibrary();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new remote_thr();
	}

	public remote_thr() {

        String connect_to;
        int message_count;
        int message_size;
        Pointer ctx = null;
        Pointer sock = null;
        int rc;
        int i;

        connect_to = "tcp://127.0.0.1:3000";
        message_size = 1;
        message_count = 1000000;
        System.out.printf("args: %s | %d | %d\n",
                          connect_to, message_size, message_count);
        NativeLong nl = new NativeLong(message_size);

        ctx = xs.xs_init();
        if (ctx == null) {
            System.out.printf("error in xs_init: %s\n",
                              xs.xs_strerror(xs.xs_errno()));
            return;
        }
        System.out.printf("XS inited\n");

        sock = xs.xs_socket(ctx, XS.PUSH);
        if (sock == null) {
            System.out.printf("error in xs_socket: %s\n",
                              xs.xs_strerror(xs.xs_errno()));
            return;
        }
        System.out.printf("XS PUSH socket created\n");

        //  Add your socket options here.

        rc = xs.xs_connect(sock, connect_to);
        if (rc == -1) {
            System.out.printf("error in xs_connect: %s\n",
                              xs.xs_strerror(xs.xs_errno()));
            return;
        }
        System.out.printf("XS PUSH socket connected to %s\n", connect_to);

        int size = 128;
        ByteBuffer bb = ByteBuffer.allocate(size);
        byte[] bba = bb.array();

        System.out.printf("XS running %d iterations...\n", message_count);
        for (i = 0; i != message_count; i++) {
            rc = xs.xs_send(sock, bba, message_size, 0);
            if (rc < 0) {
                System.out.printf("error in xs_send: %s\n",
                                  xs.xs_strerror(xs.xs_errno()));
                return;
            }
            if (rc != message_size) {
                System.out.printf("message of incorrect size sent\n");
                return;
            }
        }

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
