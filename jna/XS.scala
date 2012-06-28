/*
    Copyright (c) 2012 the original author or authors.

    This file is part of scala-xs-binding project.

    scala-xs-binding is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation; either version 3 of the License, or
    (at your option) any later version.

    scala-xs-binding is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package io.crossroads.jna;

import com.sun.jna._
import com.sun.jna.ptr._
import java.nio.ByteBuffer
import io.crossroads.XSException
import scala.collection.mutable.HashSet
import java.util.LinkedList
import io.crossroads.jna._
import scala.util.control.Breaks._
import java.util.Arrays

object XS {
  val xs: CrossroadsIOLibrary = CrossroadsIO.loadLibrary
  var majorVersion: Array[Int] = new Array(1)
  var minorVersion: Array[Int] = new Array(1)
  var patchVersion: Array[Int] = new Array(1)

  val DONTWAIT = CrossroadsIO.XS_DONTWAIT
  val SNDMORE = CrossroadsIO.XS_SNDMORE
  val PAIR = CrossroadsIO.XS_PAIR
  val PUB = CrossroadsIO.XS_PUB
  val SUB = CrossroadsIO.XS_SUB
  val REQ = CrossroadsIO.XS_REQ
  val REP = CrossroadsIO.XS_REP
  val XREQ = CrossroadsIO.XS_XREQ
  val XREP = CrossroadsIO.XS_XREP
  val PULL = CrossroadsIO.XS_PULL
  val PUSH = CrossroadsIO.XS_PUSH
  val SURVEYOR = CrossroadsIO.XS_SURVEYOR
  val RESPONDENT = CrossroadsIO.XS_RESPONDENT
  val XSURVEYOR = CrossroadsIO.XS_XSURVEYOR
  val XRESPONDENT = CrossroadsIO.XS_XRESPONDENT
  val XS_FILTER_ALL = CrossroadsIO.XS_FILTER_ALL
  val XS_FILTER_PREFIX = CrossroadsIO.XS_FILTER_PREFIX
  val XS_FILTER_TOPIC = CrossroadsIO.XS_FILTER_TOPIC
  
  
  xs.xs_version(majorVersion, minorVersion, patchVersion)

  def getMajorVersion() = majorVersion(0)

  def getMinorVersion() = minorVersion(0)

  def getPatchVersion() = patchVersion(0)

  def getFullVersion() = makeVersion(getMajorVersion(), getMinorVersion(), getPatchVersion())

  def makeVersion(major: Int, minor: Int, patch: Int) = major * 10000 + minor * 100 + patch

  def getVersionString() = "%d.%d.%d".format(getMajorVersion(), getMinorVersion(), getPatchVersion())

  def context() = new Context()

  class Context {
    val ptr: Pointer = xs.xs_init
    
    def socket(socketType: Int) = new Socket(this, socketType)

    def poller() = new Poller(this)

    def poller(size: Int) = new Poller(this, size)
    
    def setMaxSockets(optval: Int): Unit = setLongContopt(CrossroadsIO.XS_MAX_SOCKETS, optval)
    
    def setIOThreads(optval: Int): Unit = setLongContopt(CrossroadsIO.XS_IO_THREADS, optval)
    
    def setLongContopt(option: Int, optval: Long): Unit = {
      val length: NativeLong = new NativeLong(java.lang.Long.SIZE / 8)
      val value: Memory = new Memory(java.lang.Long.SIZE / 8)
      value.setLong(0, optval)
      val result:Int = xs.xs_setctxopt(ptr, option, value, length);
      if(result < 0 ) 
    	  raiseXSException();
    }
    
    def term(): Unit = {
    	val result = xs.xs_term(ptr)
    	if(result < 0 ) 
    	  raiseXSException();
    }
    
    def raiseXSException(): Unit =  {
      val errno = xs.xs_errno
      val reason = xs.xs_strerror(errno);
      throw new XSException(reason, errno);
    }
  }

  class Socket(context: Context, socketType: Int) {
  	var ptr: Pointer = xs.xs_socket(context.ptr, socketType)
  	
    var messageDataBuffer: MessageDataBuffer = new MessageDataBuffer()
    
    def close(): Int = {
  		val result = xs.xs_close(ptr)
		if(result < 0) 
    	  raiseXSException()
    	result
	}

  	def getType(): Int = getIntSockopt(CrossroadsIO.XS_TYPE)
	  
	def getLinger(): Int = getIntSockopt(CrossroadsIO.XS_LINGER)

	def getReconnectIVL(): Int = getIntSockopt(CrossroadsIO.XS_RECONNECT_IVL)

	def getBacklog(): Int = getIntSockopt(CrossroadsIO.XS_BACKLOG)

	def getReconnectIVLMax():Int = getIntSockopt(CrossroadsIO.XS_RECONNECT_IVL_MAX)

	def getMaxMsgSize(): Long = getLongSockopt(CrossroadsIO.XS_MAXMSGSIZE)

	def getSndHWM(): Int = getIntSockopt(CrossroadsIO.XS_SNDHWM)

	def getRcvHWM(): Int = getIntSockopt(CrossroadsIO.XS_RCVHWM)

	def getAffinity(): Long = getLongSockopt(CrossroadsIO.XS_AFFINITY)

	def getIdentity(): Array[Byte] = getBytesSockopt(CrossroadsIO.XS_IDENTITY)

	def getRate(): Int = getIntSockopt(CrossroadsIO.XS_RATE)

	def getRecoveryInterval(): Int = getIntSockopt(CrossroadsIO.XS_RECOVERY_IVL)

	def setReceiveTimeOut(timeout: Int): Unit = setIntSockopt(CrossroadsIO.XS_RCVTIMEO, timeout)

	def getReceiveTimeOut():Int = getIntSockopt(CrossroadsIO.XS_RCVTIMEO)

	def setSendTimeOut(timeout:Int): Unit = setIntSockopt(CrossroadsIO.XS_SNDTIMEO, timeout)

	def getSendTimeOut():Int = getIntSockopt(CrossroadsIO.XS_SNDTIMEO)

	def getSendBufferSize():Int = getIntSockopt(CrossroadsIO.XS_SNDBUF)

	def getReceiveBufferSize():Int = getIntSockopt(CrossroadsIO.XS_RCVBUF)

	def hasReceiveMore(): Boolean = getLongSockopt(CrossroadsIO.XS_RCVMORE) != 0

	def getFD(): Long = getLongSockopt(CrossroadsIO.XS_FD)

	def getEvents(): Long = getLongSockopt(CrossroadsIO.XS_EVENTS)

	def setLinger(linger: Long): Unit = setLongSockopt(CrossroadsIO.XS_LINGER, linger)

	def setReconnectIVL(reconnectIVL: Int): Unit = setIntSockopt(CrossroadsIO.XS_RECONNECT_IVL, reconnectIVL)

	def setBacklog(backlog: Int): Unit = setIntSockopt(CrossroadsIO.XS_BACKLOG, backlog)

	def setReconnectIVLMax(reconnectIVLMax: Int): Unit = setIntSockopt(CrossroadsIO.XS_RECONNECT_IVL_MAX, reconnectIVLMax)

	def setMaxMsgSize(maxMsgSize: Long): Unit = setLongSockopt(CrossroadsIO.XS_MAXMSGSIZE, maxMsgSize)

	def setSndHWM(sndHWM: Int): Unit = setIntSockopt(CrossroadsIO.XS_SNDHWM, sndHWM)

	def setRcvHWM(rcvHWM: Int): Unit = setIntSockopt(CrossroadsIO.XS_RCVHWM, rcvHWM)

	def setAffinity(affinity: Long): Unit = setLongSockopt(CrossroadsIO.XS_AFFINITY, affinity)

	def setIdentity(identity: Array[Byte]): Unit = setBytesSockopt(CrossroadsIO.XS_IDENTITY, identity)

	def subscribe(topic: Array[Byte]): Unit = setBytesSockopt(CrossroadsIO.XS_SUBSCRIBE, topic)

	def unsubscribe(topic: Array[Byte]): Unit = setBytesSockopt(CrossroadsIO.XS_UNSUBSCRIBE, topic)

	def setRate (rate: Int): Unit = setIntSockopt(CrossroadsIO.XS_RATE, rate)

	def setRecoveryInterval(recovery_ivl: Int): Unit = setIntSockopt(CrossroadsIO.XS_RECONNECT_IVL, recovery_ivl)

	def setSendBufferSize(sndbuf: Int): Unit = setIntSockopt(CrossroadsIO.XS_SNDBUF, sndbuf)

	def setReceiveBufferSize(rcvbuf: Int): Unit = setIntSockopt(CrossroadsIO.XS_RCVBUF, rcvbuf)

	def setSurveyTimeout(timeout: Int): Unit = setIntSockopt(CrossroadsIO.XS_SURVEY_TIMEOUT, timeout)
    
	def getSurveyTimeout(): Int = getIntSockopt(CrossroadsIO.XS_SURVEY_TIMEOUT)
    
	def setFilter(filter: Int): Unit = setIntSockopt(CrossroadsIO.XS_FILTER, filter)
    
	def getFilter(): Long = getIntSockopt(CrossroadsIO.XS_FILTER)
	
    def bind(addr: String): Int = {
	  	val result = xs.xs_bind(ptr, addr)
	  	if(result < 0 ) 
    	  raiseXSException()
    	result
	}

	def connect(addr: String): Int = {
	  	val result = xs.xs_connect(ptr, addr)
	  	if(result < 0 ) 
    	  raiseXSException()
    	result
	}
	
	def send(msg: Array[Byte], length: Int, flags: Int): Boolean = {
        if (xs.xs_send(ptr, msg, length, flags) < 0) {
        	raiseXSException()
            false
        } else true
	}

	def recv(length: Int, flags: Int): Array[Byte] = {
    	val bb:ByteBuffer = ByteBuffer.allocate(length)
        val bba:Array[Byte] = bb.array
        
        if(xs.xs_recv(ptr, bba, length, flags) < 0) {
            raiseXSException();
        }
    	bba
	}
      
    def sendmsg(msg: Array[Byte], flags: Int): Boolean = {
      val message: xs_msg_t = newXSMessage(msg);
      if (xs.xs_sendmsg(ptr, message, flags) == -1) {
        if (xs.xs_errno == CrossroadsIO.EAGAIN) {
          if (xs.xs_msg_close(message) < 0) {
            raiseXSException();
          } else {
            false;
          }
        } else {
          xs.xs_msg_close(message);
          raiseXSException();
          false;
        }
      }
      if (xs.xs_msg_close(message) < 0) {
        raiseXSException();
      }
      true;
    }

    def recvmsg(flags: Int): Array[Byte] = {
      val message: xs_msg_t = newXSMessage();
      
      if (xs.xs_recvmsg(ptr, message, flags) == -1) {
        if (xs.xs_errno == CrossroadsIO.EAGAIN) {
          if (xs.xs_msg_close(message) < 0) {
            raiseXSException();
          } else {
            null;
          }
        } else {
          xs.xs_msg_close(message);
          raiseXSException();
        }
      }
      val data: Pointer = xs.xs_msg_data(message);
      val length:Int = xs.xs_msg_size(message);
      val dataByteArray: Array[Byte] = data.getByteArray(0, length);
      if (xs.xs_msg_close(message) < 0) {
        raiseXSException();
      }
      dataByteArray;
    }

    def isMore(msg: Array[Byte]): Boolean = {
    	val message: xs_msg_t = newXSMessage(msg);
    	val value: Memory = new Memory(Integer.SIZE / 8);
        val length: LongByReference = new LongByReference(Integer.SIZE / 8);
        val result = xs.xs_getmsgopt(message, CrossroadsIO.XS_MORE, value, length);
        if(result < 0) {
        	raiseXSException()
  		} else {
  			if(value.getInt(0) == 1) 
        		true
        	
  		}
        false;
    }
    
    def shutdown(how: Int): Boolean = {
    	val result = xs.xs_shutdown(ptr, how);
    	if(result < 0) {
    		raiseXSException();
    		false;
    	} else true;
    }

    override def finalize(): Unit = close()

    def getIntSockopt(option: Int): Int = {
        var value: Memory = new Memory(Integer.SIZE / 8);
        val length: LongByReference = new LongByReference(Integer.SIZE / 8);
        xs.xs_getsockopt(ptr, option, value, length);
       	value.getInt(0)
    }
    
    def getLongSockopt(option: Int): Long = {
    	var value: Memory = new Memory(java.lang.Long.SIZE / 8);
      	val length: LongByReference = new LongByReference(java.lang.Long.SIZE / 8);
      	xs.xs_getsockopt(ptr, option, value, length);
      	value.getLong(0);
    }

    def setLongSockopt(option: Int, optval: Long): Unit = {
    	val length = new NativeLong(java.lang.Long.SIZE / 8);
    	val value = new Memory(java.lang.Long.SIZE / 8);
    	value.setLong(0, optval);
    	val result = xs.xs_setsockopt(ptr, option, value, length);
    	if(result < 0)
    		raiseXSException();
    }

    def setIntSockopt(option: Int, optval: Int): Unit = {
    	val length = new NativeLong(Integer.SIZE / 8);
    	var value = new Memory(Integer.SIZE / 8);
    	value.setInt(0, optval);
    	val result = xs.xs_setsockopt(ptr, option, value, length);
        if(result < 0)
        	raiseXSException();
    }
    
    def getBytesSockopt(option: Int): Array[Byte] = {
      var value = new Memory(1024);
      val length = new LongByReference(1024);
      xs.xs_getsockopt(ptr, option, value, length);
      value.getByteArray(0, length.getValue().toInt);
    }

    def setBytesSockopt(option: Int, optval: Array[Byte]): Unit = {
      val length = new NativeLong(optval.length);
      var value: Pointer = null;
      if (optval.length > 0) {
        value = new Memory(optval.length);
        value.write(0, optval, 0, optval.length);
      } else {
        value = Pointer.NULL;
      }
      val result = xs.xs_setsockopt(ptr, option, value, length);
      if(result < 0)
        	raiseXSException();
    }

    def newXSMessage(msg: Array[Byte]): xs_msg_t = {
      val message = new xs_msg_t
      if (msg.length == 0) {
        if (xs.xs_msg_init_size(message, new NativeLong(msg.length)) < 0) {
          raiseXSException();
        }
      } else {
        val mem = new Memory(msg.length);
        mem.write(0, msg, 0, msg.length);
        if (xs.xs_msg_init_data(message, mem, new NativeLong(msg.length), messageDataBuffer, mem) < 0) {
        	raiseXSException();
        } else {
        	messageDataBuffer.add(mem);
        }
      }
      message;
    }

    def newXSMessage(): xs_msg_t = {
      val message = new xs_msg_t
      if (xs.xs_msg_init(message) < 0) {
        raiseXSException();
      }
      message;
    }

    def newXSMessage(length: Int): xs_msg_t = {
        val message = new xs_msg_t
        if (xs.xs_msg_init_size(message, new NativeLong(length)) < 0) {
          raiseXSException();
        }
        message;
      }
    
    def raiseXSException(): Unit = {
      val errno = xs.xs_errno
      val reason = xs.xs_strerror(errno);
      throw new XSException(reason, errno);
    }
    
    class MessageDataBuffer extends xs_free_fn {
    	val buffer = new HashSet[Pointer]()

        def add(data: Pointer) = buffer.add(data)

        def invoke(data: Pointer, memory: Pointer) = buffer.remove(memory);
      }
  }

  class Poller(context: Context, var maxEventCount: Int = 32) {
    val POLLIN = CrossroadsIO.XS_POLLIN;
    val POLLOUT = CrossroadsIO.XS_POLLOUT;
    val POLLERR = CrossroadsIO.XS_POLLERR;

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

    def poll(): Long = {
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
      val result = xs.xs_poll(items, curEventCount, timeout);
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
    }

    def pollin(index: Int): Boolean = poll_mask(index, POLLIN)

    def pollout(index: Int): Boolean = poll_mask(index, POLLOUT)

    def pollerr(index: Int): Boolean = poll_mask(index, POLLERR)

    def poll_mask (index: Int, mask: Int): Boolean = {
      if (mask <= 0 || index < 0 || index >= nextEventIndex)
        return false
      (revents(index) & mask) > 0
    }
    
    def raiseXSException(): Unit = {
      val errno = xs.xs_errno
      val reason = xs.xs_strerror(errno)
      throw new XSException(reason, errno)
    }
  }
}
