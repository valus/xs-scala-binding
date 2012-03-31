/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.crossroads

import com.sun.jna._
import com.sun.jna.ptr._
import jnr.constants.platform.Errno


object CrossroadsIO {
	
	
	/** Unix errors */
  val EINVAL = Errno.EINVAL.intValue
  val EAGAIN = Errno.EAGAIN.intValue
  val ENODEV = Errno.ENODEV.intValue
  val ENOENT = Errno.ENOENT.intValue
  val ENOTSOCK = Errno.ENOTSOCK.intValue
  val EADDRNOTAVAIL = Errno.EADDRNOTAVAIL.intValue
  val EADDRINUSE = Errno.EADDRINUSE.intValue
  val EPROTONOSUPPORT = Errno.EPROTONOSUPPORT.intValue
  val ENOMEM = Errno.ENOMEM.intValue
  val EINTR = Errno.EINTR.intValue
  val ENOBUFS = Errno.ENOBUFS.intValue
  val ENETDOWN = Errno.ENETDOWN.intValue
  val ECONNREFUSED = Errno.ECONNREFUSED.intValue
  val EINPROGRESS = Errno.EINPROGRESS.intValue
  val EAFNOSUPPORT = Errno.EAFNOSUPPORT.intValue
  
  /** Native Crossroads errors codes */
  val XS_HAUSNUMERO = 156384712
  val EFSM = XS_HAUSNUMERO + 51
  val ENOCOMPATPROTO = XS_HAUSNUMERO + 52
  val ETERM = XS_HAUSNUMERO + 53
  val EMTHREAD = XS_HAUSNUMERO + 54
  val EFAULT = XS_HAUSNUMERO + 55
  val ENOTSUP = XS_HAUSNUMERO + 1
  val EMFILE = XS_HAUSNUMERO + 57
  
  
    /** context options */
  val XS_MAX_SOCKETS = 1
  val XS_IO_THREADS = 2
  
  
    /** Socket types */
  val XS_PAIR = 0
  val XS_PUB = 1
  val XS_SUB = 2
  val XS_REQ = 3
  val XS_REP = 4
  val XS_XREQ = 5
  val XS_XREP = 6
  val XS_PULL = 7
  val XS_PUSH = 8
  val XS_XPUB = 9
  val XS_XSUB = 10
  
  
    /** Socket options */
  val XS_AFFINITY = 4
  val XS_IDENTITY = 5
  val XS_SUBSCRIBE = 6
  val XS_UNSUBSCRIBE = 7
  val XS_RATE = 8
  val XS_RECOVERY_IVL = 9
  val XS_SNDBUF = 11
  val XS_RCVBUF = 12
  val XS_RCVMORE = 13
  val XS_FD = 14
  val XS_EVENTS = 15
  val XS_TYPE = 16
  val XS_LINGER = 17
  val XS_RECONNECT_IVL = 18
  val XS_BACKLOG = 19
  val XS_RECONNECT_IVL_MAX = 21
  val XS_MAXMSGSIZE = 22;
  val XS_SNDHWM = 23
  val XS_RCVHWM = 24
  val XS_MULTICAST_HOPS = 25
  val XS_RCVTIMEO = 27
  val XS_SNDTIMEO = 28
  val XS_IPV4ONLY = 31
  val XS_KEEPALIVE = 32
  
  
  /** Message options */
  val XS_MORE = 1
  
  
  /** Send / receive options */
  val XS_SNDMORE = 2 
  val XS_DONTWAIT = 1
  

  /** IO multiplexing */
  val XS_POLLIN: Short = 1
  val XS_POLLOUT: Short = 2
  val XS_POLLERR: Short = 4 

  
  
  /** Built-in devices 
  val XS_STREAMER = 1
  val XS_FORWARDER = 2
  val XS_QUEUE = 3
  */
  
  
  
  
  /** ZMQ message definition */
  val XS_MAX_VSM_SIZE = 30
  val XS_DELIMITER = 31
  val XS_VSM = 32
  val XS_MSG_MORE = 1
  val XS_MSG_SHARED = 128
  val XS_MSG_MASK = 129
  
  

  
  /** Helper for loading the XS library */
  def loadLibrary: CrossroadsIOLibrary = {
    Native.loadLibrary("xs", classOf[CrossroadsIOLibrary]).asInstanceOf[CrossroadsIOLibrary]  
  }
}

trait CrossroadsIOLibrary extends Library {
  def xs_bind(socket: Pointer, endpoint: String): Int
  def xs_close(socket: Pointer): Int
  def xs_connect(socket: Pointer, endpoint: String): Int
  def xs_errno: Int
  def xs_getmsgopt(message: xs_msg_t, option_name: Int, option_value: Pointer, option_len:  LongByReference): Int
  def xs_getsockopt(socket: Pointer, option_name: Int, option_value: Pointer, option_len: LongByReference): Int
  def xs_init: Pointer
  def xs_msg_init(msg: xs_msg_t): Int
  def xs_msg_close(msg: xs_msg_t): Int
  def xs_msg_copy(dest: xs_msg_t, src: xs_msg_t): Int
  def xs_msg_data(msg: xs_msg_t): Pointer
  def xs_msg_init_data(msg: xs_msg_t, data: Pointer, size: NativeLong, ffn: xs_free_fn, hint: Pointer): Int
  def xs_msg_init_size(msg: xs_msg_t, size: NativeLong): Int
  def xs_msg_move(dest: xs_msg_t, src: xs_msg_t): Int
  def xs_msg_size(msg: xs_msg_t): Int
  def xs_poll(items: Array[xs_pollitem_t], nitems: Int, timeout: Int): Int
  def xs_recvmsg(socket: Pointer, msg: xs_msg_t, flags: Int): Int
  def xs_recv(socket: Pointer, buf: Pointer, len: NativeLong, flags: Int)
  def xs_sendmsg(socket: Pointer, msg: xs_msg_t, flags: Int): Int
  def xs_send(socket: Pointer, buf: Pointer, len: NativeLong, flags: Int)
  def xs_setctxopt(socket: Pointer, option_name: Int, option_value: Pointer, option_len: NativeLong)
  def xs_setsockopt(socket: Pointer, option_name: Int, option_value: Pointer, option_len: NativeLong): Int
  def xs_socket(context: Pointer, socket_type: Int): Pointer
  def xs_strerror(errnum: Int): String
  def xs_term(context: Pointer): Int
  def xs_version(major: Array[Int], minor: Array[Int], patch: Array[Int]): Unit
  
}
