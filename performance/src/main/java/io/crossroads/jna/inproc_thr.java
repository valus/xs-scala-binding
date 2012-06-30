package io.crossroads.jna;

import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import io.crossroads.jna.*;

import com.sun.jna.Pointer;

public class inproc_thr {

	private static final CrossroadsIOLibrary xs = CrossroadsIO$.MODULE$.loadLibrary();
	
	static class Worker implements Runnable {
    public Worker(CrossroadsIOLibrary xs,
                  Pointer ctx,
                  int message_count,
                  int message_size,
                  String addr,
                  CountDownLatch done) {
        this.xs = xs;
        this.ctx = ctx;
        this.message_count = message_count;
        this.message_size = message_size;
        this.addr = addr;
        this.done = done;
    }

    public void run() {
        try {
            do_run();
            done.countDown();
        } catch (InterruptedException ex) {
        }
    }
    
    private void do_run()
        throws InterruptedException {
        Pointer sock;
        int rc;
        int i;
        
        sock = xs.xs_socket(ctx, XS.PUSH);
        if (sock == null) {
            System.out.printf("error in xs_socket: %s\n",
                              xs.xs_strerror(xs.xs_errno()));
            return;
        }
        System.out.printf("XS PUSH socket created\n");

        rc = xs.xs_connect(sock, addr);
        if (rc == -1) {
            System.out.printf("error in xs_connect(%s): %s\n",
                              addr,
                              xs.xs_strerror(xs.xs_errno()));
            return;
        }
        System.out.printf("XS PUSH socket connected to %s\n",
                          addr);

        int size = 128;
        ByteBuffer bb = ByteBuffer.allocate(size);
        byte[] bba = bb.array();

        System.out.printf("XS running %d iterations...\n",
                          message_count);
        for (i = 0; i != message_count; ++i) {
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
    }

    private CrossroadsIOLibrary xs;
    private Pointer ctx;
    private int message_count;
    private int message_size;
    private String addr;
    private CountDownLatch done;
}

public static void main(String [] args)
    throws InterruptedException {
    
    int message_count;
    int message_size;
    String addr = "inproc://thr_test";
    Pointer ctx = null;
    Pointer sock = null;
    int rc;
    int i;
    Pointer watch = null;
    long elapsed = 0;
    long throughput = 0;
    double megabits = 0.0;

    message_size = 1;
    message_count = 10000000;
    System.out.printf("args: %d | %d\n",
                      message_size, message_count);

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

    rc = xs.xs_bind(sock, addr);
    if (rc == -1) {
        System.out.printf("error in xs_bind(%s): %s\n",
                          addr,
                          xs.xs_strerror(xs.xs_errno()));
        return;
    }
    System.out.printf("XS PULL socket bound to %s\n", addr);

    int size = 128;
    ByteBuffer bb = ByteBuffer.allocate(size);
    byte[] bba = bb.array();
    CountDownLatch done = new CountDownLatch(1);

    new Thread(new Worker(xs,
                          ctx,
                          message_count,
                          message_size,
                          addr,
                          done)).start();
    
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
    
    System.out.printf("XS running %d iterations...\n",
                      message_count - 1);
    for (i = 0; i != message_count - 1; ++i) {
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

    done.await();
    
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
