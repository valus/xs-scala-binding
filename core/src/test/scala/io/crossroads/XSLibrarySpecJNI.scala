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
import io.crossroads.jni._
import io.crossroads.jni.XSLibrary


class CrossroadsIOLibrarySpecJNI extends WordSpec with MustMatchers with BeforeAndAfter {
  "CrossroadsIOLibrary - JNI" must {
    var xs = XSLibrary
    var endpoint: String = null
    before {
      endpoint = "inproc://xs-spec"
  	  
    }
    "xs_setctxopt" in {
    	val context = xs.xs_init
    	val (offset, sizeInBytes, optionValue) = (0, 4, 1)
    	
    	xs.xs_setctxoptInt(context, XSLibrary.XS_MAX_SOCKETS, optionValue) must equal(0)
    	xs.xs_term(context)
    }
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
    "xs_(get|set)sockopt" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XSLibrary.XS_PUB)
      val (offset, sizeInBytes, optionValue) = (0, 4, 1234)
      
      xs.xs_setsockoptInt(socket, XSLibrary.XS_SNDHWM, optionValue) must equal(0)
      xs.xs_getsockoptInt(socket, XSLibrary.XS_SNDHWM, 0) must equal(1234)
      
      xs.xs_close(socket)
      xs.xs_term(context)
    }
    "xs_init" in { 
      val context = xs.xs_init
      context must not be (0)
      xs.xs_term(context) must be (0)
      
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
    }*/
    "xs_(poll)" in { 
      val context = xs.xs_init
      val (push, pull) = (xs.xs_socket(context, XSLibrary.XS_PUSH), xs.xs_socket(context, XSLibrary.XS_PULL))
      xs.xs_bind(push, endpoint)
      xs.xs_connect(pull, endpoint)
      
      val testMsg = "outgoingMsg"
    
      xs.xs_send(push, testMsg.getBytes, testMsg.getBytes.length, 0) must equal(testMsg.getBytes.length)
      val items = new Array[xs_pollitem_t](1)
      items(0) = new xs_pollitem_t
      items(0).socket = pull
      items(0).events = XSLibrary.XS_POLLIN
      xs.xs_poll(items, 1, -1) must equal(1)
      new String(XSLibrary.xs_recv(pull, testMsg.getBytes, testMsg.getBytes.length, 0)) must equal(testMsg)
      
      xs.xs_close(push)
      xs.xs_close(pull)
      xs.xs_term(context)
    }
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
      socket must not be (0)
      xs.xs_close(socket) must equal(0)
      xs.xs_term(context) must equal(0)
    }
    "xs_strerror" in { 
      xs.xs_init
      xs.xs_strerror(XSLibrary.ETERM) must equal("Context was terminated")
    }
    "xs_term" in { 
      val context = xs.xs_init
      xs.xs_term(context) must equal(0)
    }
    /*"xs_version" in {
      val (major_x, minor_x, patch_x) = (Array(1), Array(1), Array(1))
      val result = xs.xs_version(major_x, minor_x, patch_x)
      result must not be (0)
    }*/
    "socket options: XS_RCVHWM" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XSLibrary.XS_PUB)
      val (offset, sizeInBytes, optionValue) = (0, 4, 1234)
      xs.xs_setsockoptInt(socket, XSLibrary.XS_RCVHWM, optionValue) must equal(0)
      xs.xs_getsockoptInt(socket, XSLibrary.XS_RCVHWM, 0) must equal(1234)
      
      xs.xs_close(socket)
      xs.xs_term(context)
    }
    "socket options: XS_LINGER" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XSLibrary.XS_PUB)
      val (offset, sizeInBytes, optionValue) = (0, 4, 1234)
      xs.xs_setsockoptInt(socket, XSLibrary.XS_LINGER, optionValue) must equal(0)
      xs.xs_getsockoptInt(socket, XSLibrary.XS_LINGER, 0) must equal(1234)
      xs.xs_close(socket)
      xs.xs_term(context)
    }
    "socket options: XS_RECONNECT_IVL" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XSLibrary.XS_PUB)
      val (offset, sizeInBytes, optionValue) = (0, 4, 1234)
      xs.xs_setsockoptInt(socket, XSLibrary.XS_RECONNECT_IVL, optionValue) must equal(0)
      xs.xs_getsockoptInt(socket, XSLibrary.XS_RECONNECT_IVL, 0) must equal(1234)
      xs.xs_close(socket)
      xs.xs_term(context)
    }
    "socket options: XS_BACKLOG" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XSLibrary.XS_PUB)
      val (offset, sizeInBytes, optionValue) = (0, 4, 1234)
      xs.xs_setsockoptInt(socket, XSLibrary.XS_BACKLOG, optionValue) must equal(0)
      xs.xs_getsockoptInt(socket, XSLibrary.XS_BACKLOG, 0) must equal(1234)
      xs.xs_close(socket)
      xs.xs_term(context)
    }
    "socket options: XS_RECONNECT_IVL_MAX" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XSLibrary.XS_PUB)
      val (offset, sizeInBytes, optionValue) = (0, 4, 1234)
      xs.xs_setsockoptInt(socket, XSLibrary.XS_RECONNECT_IVL_MAX, optionValue) must equal(0)
      xs.xs_getsockoptInt(socket, XSLibrary.XS_RECONNECT_IVL_MAX, 0) must equal(1234)
      xs.xs_close(socket)
      xs.xs_term(context)
    }
    "socket options: XS_RCVTIMEO" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XSLibrary.XS_PUB)
      val (offset, sizeInBytes, optionValue) = (0, 4, 1234)
      xs.xs_setsockoptInt(socket, XSLibrary.XS_RCVTIMEO, optionValue) must equal(0)
      xs.xs_getsockoptInt(socket, XSLibrary.XS_RCVTIMEO, 0) must equal(1234)
      xs.xs_close(socket)
      xs.xs_term(context)
    }
    "socket options: XS_IPV4ONLY" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XSLibrary.XS_PUB)
      val (offset, sizeInBytes, optionValue) = (0, 4, 0)
      xs.xs_setsockoptInt(socket, XSLibrary.XS_IPV4ONLY, optionValue) must equal(0)
      xs.xs_getsockoptInt(socket, XSLibrary.XS_IPV4ONLY, 0) must equal(0)
      xs.xs_close(socket)
      xs.xs_term(context)
    }
    "socket options: XS_KEEPALIVE" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XSLibrary.XS_PUB)
      val (offset, sizeInBytes, optionValue) = (0, 4, 1)
      xs.xs_setsockoptInt(socket, XSLibrary.XS_KEEPALIVE, optionValue) must equal(0)
      xs.xs_getsockoptInt(socket, XSLibrary.XS_KEEPALIVE, 0) must equal(1)
      xs.xs_close(socket)
      xs.xs_term(context)
    }
    "socket options: XS_SURVEY_TIMEOUT" in {
      val context = xs.xs_init
      val socket = xs.xs_socket(context, XSLibrary.XS_SURVEYOR)
      val (offset, sizeInBytes, optionValue) = (0, 4, 1)
      xs.xs_setsockoptInt(socket, XSLibrary.XS_SURVEY_TIMEOUT, optionValue) must equal(0)
      xs.xs_getsockoptInt(socket, XSLibrary.XS_SURVEY_TIMEOUT, 0) must equal(1)
      xs.xs_close(socket)
      xs.xs_term(context)
    }
  }
  def randomPort = 1024 + new Random(System.currentTimeMillis).nextInt(4096)
  lazy val dataBytes = "hello world".getBytes
}

class Task(sub: Long, testMsg: String) extends Runnable {
	
	def run(): Unit = {
		Thread.sleep(500)
		System.out.println(new String(XSLibrary.xs_recv(sub, testMsg.getBytes, testMsg.getBytes.length, 0)))
	}
}

class Task2(items: Array[xs_pollitem_t]) extends Runnable {
	
	def run(): Unit = {
		XSLibrary.xs_poll(items, 1, -1)
	}
}