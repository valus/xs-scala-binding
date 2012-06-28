package io.crossroads.jni

import io.crossroads.XSException
import java.lang.Override
import java.util.LinkedList
import io.crossroads.jni._
import scala.util.control.Breaks._
import java.nio.ByteBuffer
import java.util.Arrays

object XS {
	
  var majorVersion: Array[Int] = new Array(1)
  var minorVersion: Array[Int] = new Array(1)
  var patchVersion: Array[Int] = new Array(1)

  val DONTWAIT = XSLibrary.XS_DONTWAIT
  val SNDMORE = XSLibrary.XS_SNDMORE
  val PAIR = XSLibrary.XS_PAIR
  val PUB = XSLibrary.XS_PUB
  val SUB = XSLibrary.XS_SUB
  val REQ = XSLibrary.XS_REQ
  val REP = XSLibrary.XS_REP
  val XREQ = XSLibrary.XS_XREQ
  val XREP = XSLibrary.XS_XREP
  val PULL = XSLibrary.XS_PULL
  val PUSH = XSLibrary.XS_PUSH
  val SURVEYOR = XSLibrary.XS_SURVEYOR
  val RESPONDENT = XSLibrary.XS_RESPONDENT
  val XSURVEYOR = XSLibrary.XS_XSURVEYOR
  val XRESPONDENT = XSLibrary.XS_XRESPONDENT
  val XS_FILTER_ALL = XSLibrary.XS_FILTER_ALL
  val XS_FILTER_PREFIX = XSLibrary.XS_FILTER_PREFIX
  val XS_FILTER_TOPIC = XSLibrary.XS_FILTER_TOPIC
  
  
  //XSLibrary.xs_version(majorVersion, minorVersion, patchVersion)
  

  def getMajorVersion() = majorVersion(0)

  def getMinorVersion() = minorVersion(0)

  def getPatchVersion() = patchVersion(0)

  def getFullVersion() = makeVersion(getMajorVersion(), getMinorVersion(), getPatchVersion())

  def makeVersion(major: Int, minor: Int, patch: Int) = major * 10000 + minor * 100 + patch

  def getVersionString() = "%d.%d.%d".format(getMajorVersion(), getMinorVersion(), getPatchVersion())

  def context() = new Context()

  class Context {
    var ptr: Long = XSLibrary.xs_init()
    
    def socket(socketType: Int) = new Socket(this, socketType)

    def poller() = new Poller(this)

    def poller(size: Int) = new Poller(this, size)
    
    def setMaxSockets(optval: Int): Unit = setLongContopt(XSLibrary.XS_MAX_SOCKETS, optval)
    
    def setIOThreads(optval: Int): Unit = setLongContopt(XSLibrary.XS_IO_THREADS, optval)
    
    def setLongContopt(option: Int, optval: Long): Unit = {
      val result = XSLibrary.xs_setctxoptLong(ptr, option, optval)
      if(result < 0 ) 
    	  raiseXSException();
    }
    
    def term(): Unit = {
    	val result = XSLibrary.xs_term(ptr)
    	if(result < 0 ) 
    	  raiseXSException();
    }
    
    def raiseXSException(): Unit =  {
      val errno = XSLibrary.xs_errno();
      val reason = XSLibrary.xs_strerror(errno);
      throw new XSException(reason, errno);
    }
  }

  class Socket(context: Context, socketType: Int) {
  	  
  	var ptr: Long = XSLibrary.xs_socket(context.ptr, socketType)
	//var MessageDataBuffer messageDataBuffer = new MessageDataBuffer()
	  
	  
	  
	def close(): Int = {
  		val result = XSLibrary.xs_close(ptr)
		if(result < 0) 
    	  raiseXSException()
    	result
	}

	def getType(): Int = getIntSockopt(XSLibrary.XS_TYPE)
	  
	def getLinger(): Int = getIntSockopt(XSLibrary.XS_LINGER)

	def getReconnectIVL(): Int = getIntSockopt(XSLibrary.XS_RECONNECT_IVL)

	def getBacklog(): Int = getIntSockopt(XSLibrary.XS_BACKLOG)

	def getReconnectIVLMax():Int = getIntSockopt(XSLibrary.XS_RECONNECT_IVL_MAX)

	def getMaxMsgSize(): Long = getLongSockopt(XSLibrary.XS_MAXMSGSIZE)

	def getSndHWM(): Int = getIntSockopt(XSLibrary.XS_SNDHWM)

	def getRcvHWM(): Int = getIntSockopt(XSLibrary.XS_RCVHWM)

	def getAffinity(): Long = getLongSockopt(XSLibrary.XS_AFFINITY)

	def getIdentity(): Array[Byte] = getBytesSockopt(XSLibrary.XS_IDENTITY)

	def getRate(): Int = getIntSockopt(XSLibrary.XS_RATE)

	def getRecoveryInterval(): Int = getIntSockopt(XSLibrary.XS_RECOVERY_IVL)

	def setReceiveTimeOut(timeout: Int): Unit = setIntSockopt(XSLibrary.XS_RCVTIMEO, timeout)

	def getReceiveTimeOut():Int = getIntSockopt(XSLibrary.XS_RCVTIMEO)

	def setSendTimeOut(timeout:Int): Unit = setIntSockopt(XSLibrary.XS_SNDTIMEO, timeout)

	def getSendTimeOut():Int = getIntSockopt(XSLibrary.XS_SNDTIMEO)

	def getSendBufferSize():Int = getIntSockopt(XSLibrary.XS_SNDBUF)

	def getReceiveBufferSize():Int = getIntSockopt(XSLibrary.XS_RCVBUF)

	def hasReceiveMore(): Boolean = getLongSockopt(XSLibrary.XS_RCVMORE) != 0

	def getFD(): Long = getLongSockopt(XSLibrary.XS_FD)

	def getEvents(): Long = getLongSockopt(XSLibrary.XS_EVENTS)

	def setLinger(linger: Long): Unit = setLongSockopt(XSLibrary.XS_LINGER, linger)

	def setReconnectIVL(reconnectIVL: Int): Unit = setIntSockopt(XSLibrary.XS_RECONNECT_IVL, reconnectIVL)

	def setBacklog(backlog: Int): Unit = setIntSockopt(XSLibrary.XS_BACKLOG, backlog)

	def setReconnectIVLMax(reconnectIVLMax: Int): Unit = setIntSockopt(XSLibrary.XS_RECONNECT_IVL_MAX, reconnectIVLMax)

	def setMaxMsgSize(maxMsgSize: Long): Unit = setLongSockopt(XSLibrary.XS_MAXMSGSIZE, maxMsgSize)

	def setSndHWM(sndHWM: Int): Unit = setIntSockopt(XSLibrary.XS_SNDHWM, sndHWM)

	def setRcvHWM(rcvHWM: Int): Unit = setIntSockopt(XSLibrary.XS_RCVHWM, rcvHWM)

	def setAffinity(affinity: Long): Unit = setLongSockopt(XSLibrary.XS_AFFINITY, affinity)

	def setIdentity(identity: Array[Byte]): Unit = setBytesSockopt(XSLibrary.XS_IDENTITY, identity)

	def subscribe(topic: Array[Byte]): Unit = setBytesSockopt(XSLibrary.XS_SUBSCRIBE, topic)

	def unsubscribe(topic: Array[Byte]): Unit = setBytesSockopt(XSLibrary.XS_UNSUBSCRIBE, topic)

	def setRate (rate: Int): Unit = setIntSockopt(XSLibrary.XS_RATE, rate)

	def setRecoveryInterval(recovery_ivl: Int): Unit = setIntSockopt(XSLibrary.XS_RECONNECT_IVL, recovery_ivl)

	def setSendBufferSize(sndbuf: Int): Unit = setIntSockopt(XSLibrary.XS_SNDBUF, sndbuf)

	def setReceiveBufferSize(rcvbuf: Int): Unit = setIntSockopt(XSLibrary.XS_RCVBUF, rcvbuf)

	def setSurveyTimeout(timeout: Int): Unit = setIntSockopt(XSLibrary.XS_SURVEY_TIMEOUT, timeout)
    
	def getSurveyTimeout(): Int = getIntSockopt(XSLibrary.XS_SURVEY_TIMEOUT)
    
	def setFilter(filter: Int): Unit = setIntSockopt(XSLibrary.XS_FILTER, filter)
    
	def getFilter(): Long = getIntSockopt(XSLibrary.XS_FILTER)
    
	def bind(addr: String): Int = {
	  	val result = XSLibrary.xs_bind(ptr, addr)
	  	if(result < 0 ) 
    	  raiseXSException()
    	result
	}

	def connect(addr: String): Int = {
	  	val result = XSLibrary.xs_connect(ptr, addr)
	  	if(result < 0 ) 
    	  raiseXSException()
    	result
	}

	def send(msg: Array[Byte], length: Int, flags: Int): Boolean = {
        if (XSLibrary.xs_send(ptr, msg, length, flags) < 0) {
        	raiseXSException()
            false
        } else true
	}

	def recv(length: Int, flags: Int): Array[Byte] = {
    	val bb:ByteBuffer = ByteBuffer.allocate(length)
        val bba:Array[Byte] = bb.array
        
        if(XSLibrary.xs_recv(ptr, bba, length, flags) < 0) {
            raiseXSException();
        }
    	bba
	}
      
	/*def sendmsg(msg: Array[Byte], flags: Int): Boolean {
		xs_msg_t message = newXSMessage(msg);
	  	if (xs.xs_sendmsg(ptr, message, flags) == -1) {
	  		if (xs.xs_errno() == XSLibrary$.MODULE$.EAGAIN()) {
	  			if (xs.xs_msg_close(message) < 0) {
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
	  	if (xs.xs_msg_close(message) < 0) {
	  		raiseXSException();
	  	}
	  	return true;
	}

    def recvmsg(int flags): Array[Byte] {
      xs_msg_t message = newXSMessage();
      
      if (xs.xs_recvmsg(ptr, message, flags) == -1) {
        if (xs.xs_errno() == XSLibrary$.MODULE$.EAGAIN()) {
          if (xs.xs_msg_close(message) < 0) {
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
      if (xs.xs_msg_close(message) < 0) {
        raiseXSException();
      }
      return dataByteArray;
    }

    def isMore(msg: Array[Byte]): Boolean = {
    	xs_msg_t message = newXSMessage(msg);
    	Memory value = new Memory(Integer.SIZE / 8);
        LongByReference length = new LongByReference(Integer.SIZE / 8);
        int result = XSLibrary$.MODULE$.XS_MORE().xs_getmsgopt(message, XSLibrary$.MODULE$.XS_MORE(), value, length);
        if(result < 0) {
        	raiseXSException();
  		} else {
  			if(value.getInt(0) == 1) 
        		return true;
        	
  		}
        return false;
    }*/
    
    def shutdown(how: Int): Boolean = {
    	val result = XSLibrary.xs_shutdown(ptr, how);
    	if(result < 0) {
    		raiseXSException();
    		false;
    	} else true;
    }

    override def finalize(): Unit = close()

    def getIntSockopt(option: Int): Int = {
        var value: Int = -1
        XSLibrary.xs_getsockoptInt(ptr, option, value)
       	value
    }
    
    def getLongSockopt(option: Int): Long = {
    	var value: Long = -1
    	XSLibrary.xs_getsockoptLong(ptr, option, value)
    	value
    }

    def setLongSockopt(option: Int, optval: Long): Unit = {
    	val result = XSLibrary.xs_setsockoptLong(ptr, option, optval)
    	if(result < 0)
    		raiseXSException();
    }

    def setIntSockopt(option: Int, optval: Int): Unit = {
    	val result = XSLibrary.xs_setsockoptInt(ptr, option, optval)
        if(result < 0)
        	raiseXSException();
    }
    
    def getBytesSockopt(option: Int): Array[Byte] = {
      var value: Array[Byte] = null
      val result = XSLibrary.xs_getsockoptByte(ptr, option, value)
      value
    }

    def setBytesSockopt(option: Int, optval: Array[Byte]): Unit = {
      val result = XSLibrary.xs_setsockoptByte(ptr, option, optval)
      if(result < 0)
        raiseXSException()
    }

    /*private xs_msg_t newXSMessage(byte[] msg) {
      xs_msg_t message = new xs_msg_t();
      if (msg.length == 0) {
        if (xs.xs_msg_init_size(message, new NativeLong(msg.length)) < 0) {
          raiseXSException();
        }
      } else {
        Memory mem = new Memory(msg.length);
        mem.write(0, msg, 0, msg.length);
        if (xs.xs_msg_init_data(message, mem, new NativeLong(msg.length), messageDataBuffer, mem) < 0) {
        	raiseXSException();
        } else {
        	messageDataBuffer.add(mem);
        }
      }
      return message;
    }

    private xs_msg_t newXSMessage() {
      xs_msg_t message = new xs_msg_t();
      if (xs.xs_msg_init(message) < 0) {
        raiseXSException();
      }
      return message;
    }

    private xs_msg_t newXSMessage(int length) {
        xs_msg_t message = new xs_msg_t();
        if (xs.xs_msg_init_size(message, new NativeLong(length)) < 0) {
          raiseXSException();
        }
        return message;
      }
    
    private class MessageDataBuffer implements xs_free_fn {
        private Set<Pointer> buffer = new HashSet<Pointer>();

        public synchronized void add(Pointer data) {
          buffer.add(data);
        }

        public synchronized void invoke(Pointer data, Pointer memory) {
          buffer.remove(memory);
        }
      }*/
    def raiseXSException(): Unit = {
      val errno = XSLibrary.xs_errno();
      val reason = XSLibrary.xs_strerror(errno);
      throw new XSException(reason, errno);
    }
  }

  class Poller(context: Context, var maxEventCount: Int = 32) {
    val POLLIN = XSLibrary.XS_POLLIN;
    val POLLOUT = XSLibrary.XS_POLLOUT;
    val POLLERR = XSLibrary.XS_POLLERR;

    val SIZE_DEFAULT = 32;
    val SIZE_INCREMENT = 16;
    val UNINITIALIZED_TIMEOUT = -2;

    var timeout = UNINITIALIZED_TIMEOUT
    var nextEventIndex = 0
    var curEventCount = 0
    var sockets: Array[Socket] = new Array[Socket](maxEventCount)
    var events: Array[Short] = new Array[Short](maxEventCount)
    var revents: Array[Short] = new Array[Short](maxEventCount)
    var freeSlots = new LinkedList[Int]()

    def register(socket: Socket): Int = register(socket, POLLIN | POLLOUT | POLLERR)

    def register(socket: Socket, numEvents: Int): Int = {
      var pos = -1
      if (!freeSlots.isEmpty) {
        pos = freeSlots.remove
      } else {
        if (nextEventIndex >= maxEventCount) {
          var newMaxEventCount = maxEventCount + SIZE_INCREMENT
          sockets = Arrays.copyOf(sockets, newMaxEventCount)
          events = Arrays.copyOf(events, newMaxEventCount)
          revents = Arrays.copyOf(revents, newMaxEventCount)
          maxEventCount = newMaxEventCount
        }
        nextEventIndex +=1
        pos = nextEventIndex
      }
      sockets(pos) = socket
      events(pos) = numEvents.toShort
      curEventCount +=1
      pos
    }

    def unregister(socket: Socket): Unit =  {
      var index = 0
      breakable {
      	while(index < nextEventIndex) {
	      	if (sockets(index) == socket) {
	          unregisterSocketAtIndex(index);
	          break;
	        }
	      	index +=1
	    }
      }
    }

    def unregisterSocketAtIndex(index: Int): Unit = {
      sockets(index) = null
      events(index) = 0
      revents(index) = 0
      curEventCount -=1
      freeSlots.add(index)
    }

    def getSocket(index: Int): Socket = {
      if (index < 0 || index >= nextEventIndex) {
          return null
      }
      sockets(index)
    }

    def getTimeout(): Long = timeout

    def setTimeout(timeout: Int): Unit = this.timeout = timeout

    def getSize(): Int = maxEventCount

    def getNext(): Int = nextEventIndex

    /*def poll(): Long = {
      var timeout = -1;
      if (this.timeout != UNINITIALIZED_TIMEOUT) {
        timeout = this.timeout;
      }
      poll(timeout);
    }

    def poll(timeout: Int): Long = {
      var pollItemCount = 0
      var i = 0
      while(i < nextEventIndex) {
      	revents(i) = 0
      	i +=1
      }
      if (curEventCount == 0)
        return 0

      var items: Array[xs_pollitem_t] =  Array.fill(curEventCount)(new xs_pollitem_t)
     
      var socketIndex = 0
      while (socketIndex < sockets.length) {
        if (sockets(socketIndex) != null) {
          items(pollItemCount).socket = sockets(socketIndex).ptr;
          items(pollItemCount).fd = 0;
          items(pollItemCount).events = events(socketIndex);
          items(pollItemCount).revents = 0;
          pollItemCount +=1
        }
        socketIndex +=1
      }
      if (pollItemCount != curEventCount)
        return 0
      pollItemCount = 0;
      val result = XSLibrary.xs_poll(items, curEventCount, timeout);
      if(result < 0)
      	raiseXSException();
      socketIndex = 0
      while (socketIndex < sockets.length) {
      	if (sockets(socketIndex) != null) {
          revents(socketIndex) = items(pollItemCount).revents;
          pollItemCount += 1
        }
        socketIndex +=1
      }
      result
    }*/

    def pollin(index: Int): Boolean = poll_mask(index, POLLIN)

    def pollout(index: Int): Boolean = poll_mask(index, POLLOUT)

    def pollerr(index: Int): Boolean = poll_mask(index, POLLERR)

    def poll_mask (index: Int, mask: Int): Boolean = {
      if (mask <= 0 || index < 0 || index >= nextEventIndex)
        return false
      (revents(index) & mask) > 0
    }
    
    def raiseXSException(): Unit = {
      val errno = XSLibrary.xs_errno
      val reason = XSLibrary.xs_strerror(errno)
      throw new XSException(reason, errno)
    }
  }
}