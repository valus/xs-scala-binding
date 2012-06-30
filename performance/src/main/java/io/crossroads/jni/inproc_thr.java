package io.crossroads.jni;

import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;

public class inproc_thr {

	
	static class Worker implements Runnable {
    public Worker(long ctx,
                  int message_count,
                  int message_size,
                  String addr,
                  CountDownLatch done) {
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
        long sock;
        int rc;
        int i;
        
        sock = XSLibrary.xs_socket(ctx, XS.PUSH());
        if (sock < 0) {
            System.out.printf("error in xs_socket: %s\n",
            		XSLibrary.xs_strerror(XSLibrary.xs_errno()));
            return;
        }
        System.out.printf("XS PUSH socket created\n");

        rc = XSLibrary.xs_connect(sock, addr);
        if (rc < 0) {
            System.out.printf("error in xs_connect(%s): %s\n",
                              addr,
                              XSLibrary.xs_strerror(XSLibrary.xs_errno()));
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

        rc = xs.xs_close(sock);
        if (rc != 0) {
            System.out.printf("error in xs_close: %s\n",
            		XSLibrary.xs_strerror(XSLibrary.xs_errno()));
            return;
        }
    }

    private XSLibrary xs;
    private long ctx;
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
    long ctx = -1;
    long sock = -1;
    int rc;
    int i;
    long watch = -1;
    long elapsed = 0;
    long throughput = 0;
    double megabits = 0.0;

    message_size = 1;
    message_count = 1000000;
    System.out.printf("args: %d | %d\n",
                      message_size, message_count);

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

    rc = XSLibrary.xs_bind(sock, addr);
    if (rc == -1) {
        System.out.printf("error in xs_bind(%s): %s\n",
                          addr,
                          XSLibrary.xs_strerror(XSLibrary.xs_errno()));
        return;
    }
    System.out.printf("XS PULL socket bound to %s\n", addr);

    int size = 128;
    ByteBuffer bb = ByteBuffer.allocate(size);
    byte[] bba = bb.array();
    CountDownLatch done = new CountDownLatch(1);

    new Thread(new Worker(ctx,
                          message_count,
                          message_size,
                          addr,
                          done)).start();
    
    bba = XSLibrary.xs_recv(sock, bba, size, 0);
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
    
    System.out.printf("XS running %d iterations...\n",
                      message_count - 1);
    for (i = 0; i != message_count - 1; ++i) {
        bba = XSLibrary.xs_recv(sock, bba, size, 0);
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

    done.await();
    
    System.out.printf("message size: %d [B]\n", message_size);
    System.out.printf("message count: %d\n", message_count);
    System.out.printf("mean throughput: %d [msg/s]\n", (int) throughput);
    System.out.printf("mean throughput: %.3f [Mb/s]\n", megabits);

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
