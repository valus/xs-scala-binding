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

import java.util.concurrent.{Executors, TimeUnit}
import org.scalatest.BeforeAndAfter
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import scala.util.Random
import com.sun.jna._
import com.sun.jna.ptr._
import io.crossroads.jni._
import io.crossroads.jni.XSLibrary


class CrossroadsIOLibrarySpecJNI extends WordSpec with MustMatchers with BeforeAndAfter {
  "CrossroadsIOLibrary - JNI" must {
    var xs = XSLibrary
    var endpoint: String = null
    before {
      endpoint = "inproc://xs-spec"
  	  
    }
    /*"xs_setctxopt" in {
    	val context = xs.xs_init
    	val (offset, sizeInBytes, optionValue) = (0, 4, 1)
    	val value = new Memory(sizeInBytes) { setInt(offset, optionValue) }
    	val (length, lengthRef) = (new NativeLong(sizeInBytes), new LongByReference(sizeInBytes))
    	xs.xs_setctxopt(context, CrossroadsIO.XS_MAX_SOCKETS, value, length) must equal(0)
    	xs.xs_term(context)
    }*/
    "xs_bind" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XSLibrary.XS_PUB)
      xs.xs_bind(socket, endpoint) must equal(0)
      xs.xs_close(socket)
      xs.xs_term(context)
    }
    "xs_close" in { 
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XSLibrary.XS_PUB)
      xs.xs_close(socket) must equal(0)
      xs.xs_term(context)
    }
    "xs_connect" in {
      val context = xs.xs_init
      val (pub, sub) = (xs.xs_socket(context, XSLibrary.XS_PUB), xs.xs_socket(context, XSLibrary.XS_SUB))
      xs.xs_bind(pub, endpoint)
      xs.xs_connect(sub, endpoint) must equal(0)
      xs.xs_close(sub)
      xs.xs_close(pub)
      xs.xs_term(context)
    }
    /*"xs_(get|set)sockopt" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XSLibrary.XS_PUB)
      val (offset, sizeInBytes, optionValue) = (0, 4, 1234)
      val value = new Memory(sizeInBytes) { setInt(offset, optionValue) }
      val (length, lengthRef) = (new NativeLong(sizeInBytes), new LongByReference(sizeInBytes))
      xs.xs_setsockopt(socket, XSLibrary.XS_SNDHWM, value, length) must equal(0)
      xs.xs_getsockopt(socket, XSLibrary.XS_SNDHWM, value, lengthRef) must equal(0)
      value.getInt(offset) must equal(optionValue)
      xs.xs_close(socket)
      xs.xs_term(context)
    }*/
    "xs_init" in { 
      val context = xs.xs_init
      context must not be (null)
      xs.xs_term(context)
    }
    /*"xs_msg_close" in {
      val msg = new xs_msg_t
      xs.xs_msg_init(msg)
      xs.xs_msg_close(msg) must equal(0)
    }
    "xs_msg_copy" in {
      val (dst, src) = (new xs_msg_t, new xs_msg_t)
      xs.xs_msg_init_data(src, dataMemory, new NativeLong(dataBytes.length), null, null)
      xs.xs_msg_init_size(dst, new NativeLong(dataBytes.length)) must equal(0)
      xs.xs_msg_copy(dst, src) must equal(0)
      xs.xs_msg_close(dst)
      xs.xs_msg_close(src)
    }
    "xs_msg_data" in { 
      val msg = new xs_msg_t
      xs.xs_msg_init(msg)
      xs.xs_msg_init_data(msg, dataMemory, new NativeLong(dataBytes.length), null, null)
      xs.xs_msg_data(msg).getByteArray(0, dataBytes.length) must equal(dataBytes)
      xs.xs_msg_close(msg)
    }
    "xs_msg_init_data" in { 
      val msg = new xs_msg_t
      xs.xs_msg_init_data(msg, dataMemory, new NativeLong(dataBytes.length), null, null) must equal(0)
      xs.xs_msg_close(msg)
    }
    "xs_msg_init_size" in { 
      val msg = new xs_msg_t
      xs.xs_msg_init_size(msg, new NativeLong(dataBytes.length)) must equal(0)
      xs.xs_msg_close(msg)
    }
    "xs_msg_init" in {
      val msg = new xs_msg_t
      xs.xs_msg_init(msg) must equal(0)
      xs.xs_msg_close(msg)
    }
    "xs_msg_move" in { 
      val (dst, src) = (new xs_msg_t, new xs_msg_t)
      xs.xs_msg_init_data(src, dataMemory, new NativeLong(dataBytes.length), null, null)
      xs.xs_msg_init(dst)
      xs.xs_msg_move(dst, src) must equal(0)
      xs.xs_msg_close(dst)
      xs.xs_msg_close(src)
    }
    "xs_msg_size" in {
      val msg = new xs_msg_t
      xs.xs_msg_init_size(msg, new NativeLong(dataBytes.length))
      xs.xs_msg_size(msg) must equal(dataBytes.length)
      xs.xs_msg_close(msg)
    }
    "xs_(poll|sendmsg|recvmsg)" in { 
      val context = xs.xs_init
      val (pub, sub) = (xs.xs_socket(context, XSLibrary.XS_PUB), xs.xs_socket(context, XSLibrary.XS_SUB))
      xs.xs_bind(pub, endpoint)
      xs.xs_connect(sub, endpoint)
      xs.xs_setsockopt(sub, XSLibrary.XS_SUBSCRIBE, Pointer.NULL, new NativeLong(0))
      val (outgoingMsg, incomingMsg) = (new xs_msg_t, new xs_msg_t)
      xs.xs_msg_init_data(outgoingMsg, dataMemory, new NativeLong(dataBytes.length), null, null)
      xs.xs_msg_init(incomingMsg)
      xs.xs_recvmsg(sub, incomingMsg, XSLibrary.XS_DONTWAIT) must equal(-1)
      xs.xs_errno must equal(XSLibrary.EAGAIN)
      xs.xs_sendmsg(pub, outgoingMsg, 0) must equal(11)
      val items = new xs_pollitem_t().toArray(1).asInstanceOf[Array[xs_pollitem_t]]
      items(0) = new xs_pollitem_t
      items(0).socket = sub
      items(0).events = XSLibrary.XS_POLLIN
      xs.xs_poll(items, 1, -1) must equal(1)
      xs.xs_recvmsg(sub, incomingMsg, 0) must equal(11)
      xs.xs_msg_close(outgoingMsg)
      xs.xs_close(sub)
      xs.xs_close(pub)
      xs.xs_term(context)
    }*/
    "xs_shutdown" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XSLibrary.XS_PUB)
      val id = xs.xs_bind(socket, "tcp://127.0.0.1:3000")
      val rc = xs.xs_shutdown(socket, id)
      rc must equal(0)
      xs.xs_close(socket)
    }
    "xs_socket" in { 
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XSLibrary.XS_PUB)
      socket must not be (null)
      xs.xs_close(socket)
      xs.xs_term(context)
    }
    "xs_strerror" in { 
      xs.xs_init
      xs.xs_strerror(XSLibrary.ETERM) must equal("Context was terminated")
    }
    "xs_term" in { 
      val context = xs.xs_init
      xs.xs_term(context) must equal(0)
      xs.xs_term(context)
    }
    "xs_version" in {
      val (major_x, minor_x, patch_x) = (Array(1), Array(1), Array(1))
      val (major_y, minor_y, patch_y) = (Array(1), Array(1), Array(1))
      xs.xs_version(major_x, minor_x, patch_x)
      xs.xs_version(major_y, minor_y, patch_y)
      (major_x(0), minor_x(0), patch_x(0)) must equal(major_y(0), minor_y(0), patch_y(0))
    }
    /*"socket options: XS_RCVHWM" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XSLibrary.XS_PUB)
      val (offset, sizeInBytes, optionValue) = (0, 4, 1234)
      val value = new Memory(sizeInBytes) { setInt(offset, optionValue) }
      val (length, lengthRef) = (new NativeLong(sizeInBytes), new LongByReference(sizeInBytes))
      xs.xs_setsockopt(socket, XSLibrary.XS_RCVHWM, value, length) must equal(0)
      xs.xs_getsockopt(socket, XSLibrary.XS_RCVHWM, value, lengthRef) must equal(0)
      value.getInt(offset) must equal(optionValue)
      xs.xs_close(socket)
      xs.xs_term(context)
    }
    "socket options: XS_LINGER" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XSLibrary.XS_PUB)
      val (offset, sizeInBytes, optionValue) = (0, 4, 1234)
      val value = new Memory(sizeInBytes) { setInt(offset, optionValue) }
      val (length, lengthRef) = (new NativeLong(sizeInBytes), new LongByReference(sizeInBytes))
      xs.xs_setsockopt(socket, XSLibrary.XS_LINGER, value, length) must equal(0)
      xs.xs_getsockopt(socket, XSLibrary.XS_LINGER, value, lengthRef) must equal(0)
      value.getInt(offset) must equal(optionValue)
      xs.xs_close(socket)
      xs.xs_term(context)
    }
    "socket options: XS_RECONNECT_IVL" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, CrossroadsIO.XS_PUB)
      val (offset, sizeInBytes, optionValue) = (0, 4, 1234)
      val value = new Memory(sizeInBytes) { setInt(offset, optionValue) }
      val (length, lengthRef) = (new NativeLong(sizeInBytes), new LongByReference(sizeInBytes))
      xs.xs_setsockopt(socket, XSLibrary.XS_RECONNECT_IVL, value, length) must equal(0)
      xs.xs_getsockopt(socket, XSLibrary.XS_RECONNECT_IVL, value, lengthRef) must equal(0)
      value.getInt(offset) must equal(optionValue)
      xs.xs_close(socket)
      xs.xs_term(context)
    }
    "socket options: XS_BACKLOG" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XSLibrary.XS_PUB)
      val (offset, sizeInBytes, optionValue) = (0, 4, 1234)
      val value = new Memory(sizeInBytes) { setInt(offset, optionValue) }
      val (length, lengthRef) = (new NativeLong(sizeInBytes), new LongByReference(sizeInBytes))
      xs.xs_setsockopt(socket, XSLibrary.XS_BACKLOG, value, length) must equal(0)
      xs.xs_getsockopt(socket, XSLibrary.XS_BACKLOG, value, lengthRef) must equal(0)
      value.getInt(offset) must equal(optionValue)
      xs.xs_close(socket)
      xs.xs_term(context)
    }
    "socket options: XS_RECONNECT_IVL_MAX" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XSLibrary.XS_PUB)
      val (offset, sizeInBytes, optionValue) = (0, 4, 1234)
      val value = new Memory(sizeInBytes) { setInt(offset, optionValue) }
      val (length, lengthRef) = (new NativeLong(sizeInBytes), new LongByReference(sizeInBytes))
      xs.xs_setsockopt(socket, XSLibrary.XS_RECONNECT_IVL_MAX, value, length) must equal(0)
      xs.xs_getsockopt(socket, XSLibrary.XS_RECONNECT_IVL_MAX, value, lengthRef) must equal(0)
      value.getInt(offset) must equal(optionValue)
      xs.xs_close(socket)
      xs.xs_term(context)
    }
    "socket options: XS_RCVTIMEO" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XSLibrary.XS_PUB)
      val (offset, sizeInBytes, optionValue) = (0, 4, 1234)
      val value = new Memory(sizeInBytes) { setInt(offset, optionValue) }
      val (length, lengthRef) = (new NativeLong(sizeInBytes), new LongByReference(sizeInBytes))
      xs.xs_setsockopt(socket, XSLibrary.XS_RCVTIMEO, value, length) must equal(0)
      xs.xs_getsockopt(socket, XSLibrary.XS_RCVTIMEO, value, lengthRef) must equal(0)
      value.getInt(offset) must equal(optionValue)
      xs.xs_close(socket)
      xs.xs_term(context)
    }
    "socket options: XS_IPV4ONLY" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XSLibrary.XS_PUB)
      val (offset, sizeInBytes, optionValue) = (0, 4, 0)
      val value = new Memory(sizeInBytes) { setInt(offset, optionValue) }
      val (length, lengthRef) = (new NativeLong(sizeInBytes), new LongByReference(sizeInBytes))
      xs.xs_setsockopt(socket, XSLibrary.XS_IPV4ONLY, value, length) must equal(0)
      xs.xs_getsockopt(socket, XSLibrary.XS_IPV4ONLY, value, lengthRef) must equal(0)
      value.getInt(offset) must equal(optionValue)
      xs.xs_close(socket)
      xs.xs_term(context)
    }
    "socket options: XS_KEEPALIVE" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XSLibrary.XS_PUB)
      val (offset, sizeInBytes, optionValue) = (0, 4, 1)
      val value = new Memory(sizeInBytes) { setInt(offset, optionValue) }
      val (length, lengthRef) = (new NativeLong(sizeInBytes), new LongByReference(sizeInBytes))
      xs.xs_setsockopt(socket, XSLibrary.XS_KEEPALIVE, value, length) must equal(0)
      xs.xs_getsockopt(socket, XSLibrary.XS_KEEPALIVE, value, lengthRef) must equal(0)
      value.getInt(offset) must equal(optionValue)
      xs.xs_close(socket)
      xs.xs_term(context)
    }
    "socket options: XS_SURVEY_TIMEOUT" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XSLibrary.XS_SURVEYOR)
      val (offset, sizeInBytes, optionValue) = (0, 4, 1)
      val value = new Memory(sizeInBytes) { setInt(offset, optionValue) }
      val (length, lengthRef) = (new NativeLong(sizeInBytes), new LongByReference(sizeInBytes))
      xs.xs_setsockopt(socket, XSLibrary.XS_SURVEY_TIMEOUT, value, length) must equal(0)
      xs.xs_getsockopt(socket, XSLibrary.XS_SURVEY_TIMEOUT, value, lengthRef) must equal(0)
      value.getInt(offset) must equal(optionValue)
      xs.xs_close(socket)
      xs.xs_term(context)
    }*/
  }
  def randomPort = 1024 + new Random(System.currentTimeMillis).nextInt(4096)
  lazy val dataBytes = "hello world".getBytes
  lazy val dataMemory = new Memory(dataBytes.length) { write(0, dataBytes, 0, dataBytes.length) }
}
