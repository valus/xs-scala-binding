package io.crossroads.jni

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


object XSLibrary {

  //val libname = System.getProperty("XSLibrary")
  //if (libname ne null) {
  System.load("/Users/valus/workspace/xs-scala-binding/target/so/XSLibrary.so")
  /*} else {
	  println("2. java.library.path: " + System.getProperty("java.library.path"))
	  System.loadLibrary("XSLibrary")
  }*/
  
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
  val XS_PLUGIN = 3
  
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
  val XS_SURVEYOR = 11
  val XS_RESPONDENT = 12
  val XS_XSURVEYOR = 13
  val XS_XRESPONDENT = 14
  
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
  val XS_PROTOCOL = 33
  val XS_SURVEY_TIMEOUT = 35
  val XS_FILTER = 34
  
  /** Filters options */
  val XS_PLUGIN_FILTER = 1
  val XS_FILTER_ALL = 0
  val XS_FILTER_PREFIX = 1
  val XS_FILTER_TOPIC = 2
  
  /** Message options */
  val XS_MORE = 1
  
  
  /** Send / receive options */
  val XS_SNDMORE = 2 
  val XS_DONTWAIT = 1
  

  /** IO multiplexing */
  val XS_POLLIN: Short = 1
  val XS_POLLOUT: Short = 2
  val XS_POLLERR: Short = 4 

    
  /** ZMQ message definition */
  val XS_MAX_VSM_SIZE = 30
  val XS_DELIMITER = 31
  val XS_VSM = 32
  val XS_MSG_MORE = 1
  val XS_MSG_SHARED = 128
  val XS_MSG_MASK = 129
  

  @native
  def xs_bind(socket: Long, endpoint: String): Int
  @native
  def xs_close(socket: Long): Int
  @native
  def xs_connect(socket: Long, endpoint: String): Int
  @native
  def xs_errno(): Int
  //def xs_getmsgopt(message: xs_msg_t, option_name: Int, option_value: Pointer): Int
  @native
  def xs_getsockoptInt(socket: Long, option_name: Int, option_value: Int): Int
  @native
  def xs_getsockoptLong(socket: Long, option_name: Int, option_value: Long): Long
  @native
  def xs_getsockoptByte(socket: Long, option_name: Int, option_value: Array[Byte]): Array[Byte]
  @native
  def xs_init(): Long
  //def xs_msg_init(msg: xs_msg_t): Int
  //def xs_msg_close(msg: xs_msg_t): Int
  //def xs_msg_copy(dest: xs_msg_t, src: xs_msg_t): Int
  //def xs_msg_data(msg: xs_msg_t): Pointer
  //def xs_msg_init_data(msg: xs_msg_t, data: Pointer, size: NativeLong, ffn: xs_free_fn, hint: Pointer): Int
  //def xs_msg_init_size(msg: xs_msg_t, size: NativeLong): Int
  //def xs_msg_move(dest: xs_msg_t, src: xs_msg_t): Int
  //def xs_msg_size(msg: xs_msg_t): Int
  //def xs_poll(items: Array[xs_pollitem_t], nitems: Int, timeout: Int): Int
  //def xs_recvmsg(socket: Pointer, msg: xs_msg_t, flags: Int): Int
  @native
  def xs_recv(socket: Long, buf: Array[Byte], len: Int, flags: Int): Array[Byte]
  //def xs_sendmsg(socket: Long, msg: xs_msg_t, flags: Int): Int
  @native
  def xs_send(socket: Long, buf: Array[Byte], len: Int, flags: Int): Int
  @native
  def xs_setctxoptInt(socket: Long, option_name: Int, option_value: Int): Int
  @native
  def xs_setctxoptLong(socket: Long, option_name: Int, option_value: Long): Int
  @native
  def xs_setctxoptByte(socket: Long, option_name: Int, option_value: Array[Byte]): Int
  @native
  def xs_setsockoptInt(socket: Long, option_name: Int, option_value: Int): Int
  @native
  def xs_setsockoptLong(socket: Long, option_name: Int, option_value: Long): Int
  @native
  def xs_setsockoptByte(socket: Long, option_name: Int, option_value: Array[Byte]): Int
  @native
  def xs_shutdown(socket: Long, how: Int): Int
  @native
  def xs_socket(context: Long, socket_type: Int): Long
  @native
  def xs_strerror(errnum: Int): String
  @native
  def xs_term(context: Long): Int
  @native
  def xs_version(major: Array[Int], minor: Array[Int], patch: Array[Int]): Int
  @native
  def xs_stopwatch_start(): Long
  @native
  def xs_stopwatch_stop(watch: Long): Long
}
