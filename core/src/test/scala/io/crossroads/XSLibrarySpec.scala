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
import io.crossroads.CrossroadsIO._
import scala.util.Random
import com.sun.jna._
import com.sun.jna.ptr._


class CrossroadsIOLibrarySpec extends WordSpec with MustMatchers with BeforeAndAfter {
  "CrossroadsIOLibrary" must {
    var xs: CrossroadsIOLibrary = null
    var endpoint: String = null
    before {
      xs = CrossroadsIO.loadLibrary
      endpoint = "inproc://xs-spec"
    }
    "xs_setctxopt" in {
    	val context = xs.xs_init
    	val (offset, sizeInBytes, optionValue) = (0, 4, 1)
    	val value = new Memory(sizeInBytes) { setInt(offset, optionValue) }
    	val (length, lengthRef) = (new NativeLong(sizeInBytes), new LongByReference(sizeInBytes))
    	xs.xs_setctxopt(context, XS_MAX_SOCKETS, value, length) must equal(0)
    }
    "xs_bind" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XS_PUB)
      xs.xs_bind(socket, endpoint) must equal(0)
      xs.xs_close(socket)
    }
    "xs_close" in { 
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XS_PUB)
      xs.xs_close(socket) must equal(0)
    }
    "xs_connect" in {
      val context = xs.xs_init
      val (pub, sub) = (xs.xs_socket(context, XS_PUB), xs.xs_socket(context, XS_SUB))
      xs.xs_bind(pub, endpoint)
      xs.xs_connect(sub, endpoint) must equal(0)
      xs.xs_close(sub)
      xs.xs_close(pub)
    }
    "xs_errno" in { 
      xs.xs_init
      xs.xs_errno must equal(ENOENT)
    }
    "xs_(get|set)sockopt" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XS_PUB)
      val (offset, sizeInBytes, optionValue) = (0, 8, 1)
      val value = new Memory(sizeInBytes) { setInt(offset, optionValue) }
      val (length, lengthRef) = (new NativeLong(sizeInBytes), new LongByReference(sizeInBytes))
      xs.xs_setsockopt(socket, XS_AFFINITY, value, length) must equal(0)
      xs.xs_getsockopt(socket, XS_AFFINITY, value, lengthRef) must equal(0)
      value.getInt(offset) must equal(optionValue)
      xs.xs_close(socket)
    }
    "xs_init" in { 
      val context = xs.xs_init
      context must not be (null)
    }
    "xs_msg_close" in {
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
      val (pub, sub) = (xs.xs_socket(context, XS_PUB), xs.xs_socket(context, XS_SUB))
      xs.xs_bind(pub, endpoint)
      xs.xs_connect(sub, endpoint)
      xs.xs_setsockopt(sub, XS_SUBSCRIBE, Pointer.NULL, new NativeLong(0))
      val (outgoingMsg, incomingMsg) = (new xs_msg_t, new xs_msg_t)
      xs.xs_msg_init_data(outgoingMsg, dataMemory, new NativeLong(dataBytes.length), null, null)
      xs.xs_msg_init(incomingMsg)
      xs.xs_recvmsg(sub, incomingMsg, XS_DONTWAIT) must equal(-1)
      xs.xs_errno must equal(EAGAIN)
      xs.xs_sendmsg(pub, outgoingMsg, 0) must equal(11)
      val items = new xs_pollitem_t().toArray(1).asInstanceOf[Array[xs_pollitem_t]]
      items(0) = new xs_pollitem_t
      items(0).socket = sub
      items(0).events = XS_POLLIN
      xs.xs_poll(items, 1, -1) must equal(1)
      xs.xs_recvmsg(sub, incomingMsg, 0) must equal(11)
      xs.xs_msg_close(outgoingMsg)
      xs.xs_close(sub)
      xs.xs_close(pub)
    }
    "xs_shutdown" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XS_PUB)
      val id = xs.xs_bind(socket, "tcp://127.0.0.1:3000")
      val rc = xs.xs_shutdown(socket, id)
      rc must equal(0)
      xs.xs_close(socket)
    }
    "xs_socket" in { 
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XS_PUB)
      socket must not be (null)
    }
    "xs_strerror" in { 
      xs.xs_init
      xs.xs_strerror(ETERM) must equal("Context was terminated")
    }
    "xs_term" in { 
      val context = xs.xs_init
      xs.xs_term(context) must equal(0)
    }
    "xs_version" in {
      val (major_x, minor_x, patch_x) = (Array(1), Array(1), Array(1))
      val (major_y, minor_y, patch_y) = (Array(1), Array(1), Array(1))
      xs.xs_version(major_x, minor_x, patch_x)
      xs.xs_version(major_y, minor_y, patch_y)
      (major_x(0), minor_x(0), patch_x(0)) must equal(major_y(0), minor_y(0), patch_y(0))
    }
  }
  def randomPort = 1024 + new Random(System.currentTimeMillis).nextInt(4096)
  lazy val dataBytes = "hello world".getBytes
  lazy val dataMemory = new Memory(dataBytes.length) { write(0, dataBytes, 0, dataBytes.length) }
}
