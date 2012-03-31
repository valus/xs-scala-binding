package io.crossroads;

import com.sun.jna.*;
import com.sun.jna.ptr.*;
import java.util.Arrays;
import java.util.LinkedList;
import io.crossroads.CrossroadsIOLibrary;

/**
 * Offers an API similar to that of jzmq [1] written by Gonzalo Diethelm.
 *
 * 1. https://github.com/zeromq/jzmq
 */
public class XS {
  private static final CrossroadsIOLibrary xs = CrossroadsIO$.MODULE$.loadLibrary();
  private static final int[] majorVersion = new int[1];
  private static final int[] minorVersion = new int[1];
  private static final int[] patchVersion = new int[1];

  public static final int DONTWAIT = CrossroadsIO$.MODULE$.XS_DONTWAIT();
  public static final int SNDMORE = CrossroadsIO$.MODULE$.XS_SNDMORE();
  public static final int PAIR = CrossroadsIO$.MODULE$.XS_PAIR();
  public static final int PUB = CrossroadsIO$.MODULE$.XS_PUB();
  public static final int SUB = CrossroadsIO$.MODULE$.XS_SUB();
  public static final int REQ = CrossroadsIO$.MODULE$.XS_REQ();
  public static final int REP = CrossroadsIO$.MODULE$.XS_REP();
  public static final int XREQ = CrossroadsIO$.MODULE$.XS_XREQ();
  public static final int XREP = CrossroadsIO$.MODULE$.XS_XREP();
  public static final int PULL = CrossroadsIO$.MODULE$.XS_PULL();
  public static final int PUSH = CrossroadsIO$.MODULE$.XS_PUSH();

  static {
    xs.xs_version(majorVersion, minorVersion, patchVersion);
  }

  public static int getMajorVersion() {
    return majorVersion[0];
  }

  public static int getMinorVersion() {
    return minorVersion[0];
  }

  public static int getPatchVersion() {
    return patchVersion[0];
  }

  public static int getFullVersion() {
    return makeVersion(getMajorVersion(), getMinorVersion(), getPatchVersion());
  }

  public static int makeVersion(int major, int minor, int patch) {
    return major * 10000 + minor * 100 + patch;
  }

  public static String getVersionString() {
    return String.format("%d.%d.%d", getMajorVersion(), getMinorVersion(), getPatchVersion());
  }

  public static Context context() {
    return new Context();
  }

  public static class Context {
    protected Pointer ptr;

    public void term () {
    }

    public Socket socket(int type) {
      return new Socket(this, type);
    }

    public Poller poller() {
      return new Poller(this);
    }

    public Poller poller(int size) {
      return new Poller(this, size);
    }

    protected Context () {
      ptr = xs.xs_init();
    }
  }

  public static class Socket {
    protected Pointer ptr;

    public void close() {
      xs.xs_close(ptr);
    }

    public int getType() {
      return (int) getLongSockopt(CrossroadsIO$.MODULE$.XS_TYPE());
    }

    public long getLinger() {
      return (int) getLongSockopt(CrossroadsIO$.MODULE$.XS_LINGER());
    }

    public long getReconnectIVL() {
      return (int) getLongSockopt(CrossroadsIO$.MODULE$.XS_RECONNECT_IVL());
    }

    public long getBacklog() {
      return (int) getLongSockopt(CrossroadsIO$.MODULE$.XS_BACKLOG());
    }

    public long getReconnectIVLMax() {
      return getLongSockopt(CrossroadsIO$.MODULE$.XS_RECONNECT_IVL_MAX());
    }

    public long getMaxMsgSize() {
      return getLongSockopt(CrossroadsIO$.MODULE$.XS_MAXMSGSIZE());
    }

    public long getSndHWM() {
      return getLongSockopt(CrossroadsIO$.MODULE$.XS_SNDHWM());
    }

    public long getRcvHWM() {
      return getLongSockopt(CrossroadsIO$.MODULE$.XS_RCVHWM());
    }

    public long getAffinity() {
      return getLongSockopt(CrossroadsIO$.MODULE$.XS_AFFINITY());
    }

    public byte[] getIdentity() {
      return getBytesSockopt(CrossroadsIO$.MODULE$.XS_IDENTITY());
    }

    public long getRate() {
      return getLongSockopt(CrossroadsIO$.MODULE$.XS_RATE());
    }

    public long getRecoveryInterval() {
      return getLongSockopt(CrossroadsIO$.MODULE$.XS_RECOVERY_IVL());
    }

    public void setReceiveTimeOut(long timeout) {
      setLongSockopt(CrossroadsIO$.MODULE$.XS_RCVTIMEO(), timeout);
    }

    public long getReceiveTimeOut() {
      return getLongSockopt(CrossroadsIO$.MODULE$.XS_RCVTIMEO());
    }

    public void setSendTimeOut(long timeout) {
      setLongSockopt(CrossroadsIO$.MODULE$.XS_SNDTIMEO(), timeout);
    }

    public long getSendTimeOut() {
      return getLongSockopt(CrossroadsIO$.MODULE$.XS_SNDTIMEO());
    }

    public long getSendBufferSize() {
      return getLongSockopt(CrossroadsIO$.MODULE$.XS_SNDBUF());
    }

    public long getReceiveBufferSize() {
      return getLongSockopt(CrossroadsIO$.MODULE$.XS_RCVBUF());
    }

    public boolean hasReceiveMore() {
      return getLongSockopt(CrossroadsIO$.MODULE$.XS_RCVMORE()) != 0;
    }

    public long getFD() {
      return getLongSockopt(CrossroadsIO$.MODULE$.XS_FD());
    }

    public long getEvents() {
      return getLongSockopt(CrossroadsIO$.MODULE$.XS_EVENTS());
    }

    public void setLinger(long linger) {
      setLongSockopt(CrossroadsIO$.MODULE$.XS_LINGER(), linger);
    }

    public void setReconnectIVL(long reconnectIVL) {
      setLongSockopt(CrossroadsIO$.MODULE$.XS_RECONNECT_IVL(), reconnectIVL);
    }

    public void setBacklog(long backlog) {
      setLongSockopt(CrossroadsIO$.MODULE$.XS_BACKLOG(), backlog);
    }

    public void setReconnectIVLMax(long reconnectIVLMax) {
      setLongSockopt(CrossroadsIO$.MODULE$.XS_RECONNECT_IVL_MAX(), reconnectIVLMax);
    }

    public void setMaxMsgSize(long maxMsgSize) {
      setLongSockopt(CrossroadsIO$.MODULE$.XS_MAXMSGSIZE(), maxMsgSize);
    }

    public void setSndHWM(long sndHWM) {
      setLongSockopt(CrossroadsIO$.MODULE$.XS_SNDHWM(), sndHWM);
    }

    public void setRcvHWM(long rcvHWM) {
      setLongSockopt(CrossroadsIO$.MODULE$.XS_RCVHWM(), rcvHWM);
    }

    public void setAffinity(long affinity) {
      setLongSockopt(CrossroadsIO$.MODULE$.XS_AFFINITY(), affinity);
    }

    public void setIdentity(byte[] identity) {
      setBytesSockopt(CrossroadsIO$.MODULE$.XS_IDENTITY(), identity);
    }

    public void subscribe(byte[] topic) {
      setBytesSockopt(CrossroadsIO$.MODULE$.XS_SUBSCRIBE(), topic);
    }

    public void unsubscribe(byte[] topic) {
      setBytesSockopt(CrossroadsIO$.MODULE$.XS_UNSUBSCRIBE(), topic);
    }

    public void setRate (long rate) {
      setLongSockopt(CrossroadsIO$.MODULE$.XS_RATE(), rate);
    }

    public void setRecoveryInterval(long recovery_ivl) {
      setLongSockopt(CrossroadsIO$.MODULE$.XS_RECONNECT_IVL(), recovery_ivl);
    }

    public void setSendBufferSize(long sndbuf) {
      setLongSockopt(CrossroadsIO$.MODULE$.XS_SNDBUF(), sndbuf);
    }

    public void setReceiveBufferSize(long rcvbuf) {
      setLongSockopt(CrossroadsIO$.MODULE$.XS_RCVBUF(), rcvbuf);
    }

    public void bind(String addr) {
      xs.xs_bind(ptr, addr);
    }

    public void connect(String addr) {
      xs.xs_connect(ptr, addr);
    }

    public boolean send(byte[] msg, int flags) {
        xs_msg_t message = newXSMessage(msg);
        Pointer data = xs.xs_msg_data(message);
        NativeLong length = new NativeLong(msg.length);
        if (xs.xs_send(ptr, data, length, flags) == -1) {
          if (xs.xs_errno() == CrossroadsIO$.MODULE$.EAGAIN()) {
            if (xs.xs_msg_close(message) != 0) {
              raiseXSException();
            } else {
              return false;
            }
          } else {
            xs.xs_msg_close(message);
            raiseXSException();
            return false;
          }
        }
        if (xs.xs_msg_close(message) != 0) {
          raiseXSException();
        }
        return true;
      }

      public byte[] recv(int length, int flags) {
        xs_msg_t message = newXSMessage(length);
        Pointer data = xs.xs_msg_data(message);
        NativeLong len = new NativeLong(length);
        
        if (xs.xs_recv(ptr, data, len, flags) == -1) {
          if (xs.xs_errno() == CrossroadsIO$.MODULE$.EAGAIN()) {
            if (xs.xs_msg_close(message) != 0) {
              raiseXSException();
            } else {
              return null;
            }
          } else {
            xs.xs_msg_close(message);
            raiseXSException();
          }
        }
        
        byte[] dataByteArray = data.getByteArray(0, length);
        if (xs.xs_msg_close(message) != 0) {
          raiseXSException();
        }
        return dataByteArray;
      }
      
    public boolean sendmsg(byte[] msg, int flags) {
      xs_msg_t message = newXSMessage(msg);
      if (xs.xs_sendmsg(ptr, message, flags) == -1) {
        if (xs.xs_errno() == CrossroadsIO$.MODULE$.EAGAIN()) {
          if (xs.xs_msg_close(message) != 0) {
            raiseXSException();
          } else {
            return false;
          }
        } else {
          xs.xs_msg_close(message);
          raiseXSException();
          return false;
        }
      }
      if (xs.xs_msg_close(message) != 0) {
        raiseXSException();
      }
      return true;
    }

    public byte[] recvmsg(int flags) {
      xs_msg_t message = newXSMessage();
      
      if (xs.xs_recvmsg(ptr, message, flags) == -1) {
        if (xs.xs_errno() == CrossroadsIO$.MODULE$.EAGAIN()) {
          if (xs.xs_msg_close(message) != 0) {
            raiseXSException();
          } else {
            return null;
          }
        } else {
          xs.xs_msg_close(message);
          raiseXSException();
        }
      }
      Pointer data = xs.xs_msg_data(message);
      int length = xs.xs_msg_size(message);
      byte[] dataByteArray = data.getByteArray(0, length);
      if (xs.xs_msg_close(message) != 0) {
        raiseXSException();
      }
      return dataByteArray;
    }

    protected Socket(Context context, int type) {
      ptr = xs.xs_socket(context.ptr, type);
    }

    @Override protected void finalize() {
      close();
    }

    private long getLongSockopt(int option) {
      Memory value = new Memory(Long.SIZE / 8);
      LongByReference length = new LongByReference(Long.SIZE / 8);
      xs.xs_getsockopt(ptr, option, value, length);
      return value.getLong(0);
    }

    private void setLongSockopt(int option, long optval) {
      NativeLong length = new NativeLong(Long.SIZE / 8);
      Memory value = new Memory(Long.SIZE / 8);
      value.setLong(0, optval);
      xs.xs_setsockopt(ptr, option, value, length);
    }

    private byte[] getBytesSockopt(int option) {
      Memory value = new Memory(1024);
      LongByReference length = new LongByReference(1024);
      xs.xs_getsockopt(ptr, option, value, length);
      return value.getByteArray(0, (int) length.getValue());
    }

    private void setBytesSockopt(int option, byte[] optval) {
      NativeLong length = new NativeLong(optval.length);
      Pointer value = null;
      if (optval.length > 0) {
        value = value = new Memory(optval.length);
        value.write(0, optval, 0, optval.length);
      } else {
        value = Pointer.NULL;
      }
      xs.xs_setsockopt(ptr, option, value, length);
    }

    private xs_msg_t newXSMessage(byte[] msg) {
      xs_msg_t message = new xs_msg_t();
      if (msg.length == 0) {
        if (xs.xs_msg_init_size(message, new NativeLong(msg.length)) != 0) {
          raiseXSException();
        }
      } else {
        Memory mem = new Memory(msg.length);
        mem.write(0, msg, 0, msg.length);
        if (xs.xs_msg_init_data(message, mem, new NativeLong(msg.length), null, null) != 0) {
          raiseXSException();
        }
      }
      return message;
    }

    private xs_msg_t newXSMessage() {
      xs_msg_t message = new xs_msg_t();
      if (xs.xs_msg_init(message) != 0) {
        raiseXSException();
      }
      return message;
    }

    private xs_msg_t newXSMessage(int length) {
        xs_msg_t message = new xs_msg_t();
        if (xs.xs_msg_init_size(message, new NativeLong(length)) != 0) {
          raiseXSException();
        }
        return message;
      }
    
    private void raiseXSException() {
      int errno = xs.xs_errno();
      String reason = xs.xs_strerror(errno);
      throw new XSException(reason, errno);
    }
  }

  public static class Poller {
    public static final int POLLIN = CrossroadsIO$.MODULE$.XS_POLLIN();
    public static final int POLLOUT = CrossroadsIO$.MODULE$.XS_POLLOUT();
    public static final int POLLERR = CrossroadsIO$.MODULE$.XS_POLLERR();

    private static final int SIZE_DEFAULT = 32;
    private static final int SIZE_INCREMENT = 16;
    private static final int UNINITIALIZED_TIMEOUT = -2;

    private int timeout = UNINITIALIZED_TIMEOUT;
    private int nextEventIndex = 0;
    private int maxEventCount = 0;
    private int curEventCount = 0;
    private Socket[] sockets = null;
    private short[] events = null;
    private short[] revents = null;
    private LinkedList<Integer> freeSlots = null;

    public int register(Socket socket) {
      return register(socket, POLLIN | POLLOUT | POLLERR);
    }

    public int register(Socket socket, int numEvents) {
      int pos = -1;
      if (!freeSlots.isEmpty()) {
        pos = freeSlots.remove();
      } else {
        if (nextEventIndex >= maxEventCount) {
          int newMaxEventCount = maxEventCount + SIZE_INCREMENT;
          sockets = Arrays.copyOf(sockets, newMaxEventCount);
          events = Arrays.copyOf(events, newMaxEventCount);
          revents = Arrays.copyOf(revents, newMaxEventCount);
          maxEventCount = newMaxEventCount;
        }
        pos = nextEventIndex++;
      }
      sockets[pos] = socket;
      events[pos] = (short) numEvents;
      curEventCount++;
      return pos;
    }

    public void unregister(Socket socket) {
      for (int index = 0; index < nextEventIndex; index++) {
        if (sockets[index] == socket) {
          unregisterSocketAtIndex(index);
          break;
        }
      }
    }

    private void unregisterSocketAtIndex(int index) {
      sockets[index] = null;
      events[index] = 0;
      revents[index] = 0;
      freeSlots.add(index);
      curEventCount--;
    }

    public Socket getSocket(int index) {
      if (index < 0 || index >= nextEventIndex) {
          return null;
      }
      return sockets[index];
    }

    public long getTimeout() {
      return timeout;
    }

    public void setTimeout(int timeout) {
      this.timeout = timeout;
    }

    public int getSize() {
      return maxEventCount;
    }

    public int getNext() {
      return nextEventIndex;
    }

    public long poll() {
      int timeout = -1;
      if (this.timeout != UNINITIALIZED_TIMEOUT) {
        timeout = this.timeout;
      }
      return poll(timeout);
    }

    public long poll(int timeout) {
      int pollItemCount = 0;
      for (int i = 0; i < nextEventIndex; i++) {
        revents[i] = 0;
      }
      if (curEventCount == 0)
        return 0;
      xs_pollitem_t[] items = (xs_pollitem_t[]) new xs_pollitem_t().toArray(curEventCount);
      for (int i = 0; i < pollItemCount; i++) {
        items[i] = new xs_pollitem_t();
      }
      for (int socketIndex = 0; socketIndex < sockets.length; socketIndex++) {
        if (sockets[socketIndex] == null) {
          continue;
        }
        items[pollItemCount].socket = sockets[socketIndex].ptr;
        items[pollItemCount].fd = 0;
        items[pollItemCount].events = events[socketIndex];
        items[pollItemCount].revents = 0;
        pollItemCount++;
      }
      if (pollItemCount != curEventCount)
        return 0;
      pollItemCount = 0;
      int result = xs.xs_poll(items, curEventCount, timeout);
      for (int socketIndex = 0; socketIndex < sockets.length; socketIndex++) {
        if (sockets[socketIndex] == null) {
          continue;
        }
        revents[socketIndex] = items[pollItemCount].revents;
        pollItemCount++;
      }
      return result;
    }

    public boolean pollin(int index) {
      return poll_mask(index, POLLIN);
    }

    public boolean pollout(int index) {
      return poll_mask(index, POLLOUT);
    }

    public boolean pollerr(int index) {
      return poll_mask(index, POLLERR);
    }

    protected Poller(Context context) { 
      this(context, SIZE_DEFAULT);
    }

    protected Poller(Context context, int size) { 
      this.maxEventCount = size;
      this.sockets = new Socket[maxEventCount];
      this.events = new short[maxEventCount];
      this.revents = new short[maxEventCount];
      this.freeSlots = new LinkedList<Integer>();
    }

    private boolean poll_mask (int index, int mask) {
      if (mask <= 0 || index < 0 || index >= nextEventIndex) {
        return false;
      }
      return (revents[index] & mask) > 0;
    }
  }
}
