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

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import io.crossroads.XS._


class XSSpec extends WordSpec with MustMatchers {
  "XS" must {
	  "support Socket#getType" in {
		  val context = XS.context
		  val sub = context.socket(XS.PAIR)
		  sub.getType must equal(XS.PAIR)
		  sub.close 
		  context.term
	  }
	  "support pub-sub connection pattern" in {
		  val context = XS.context
		  val (pub, sub, poller) = (
		  	context.socket(XS.PUB), 
		  	context.socket(XS.SUB), 
		  	context.poller
		  )
		  pub.bind("inproc://xs-spec")
		  sub.connect("inproc://xs-spec")
		  sub.subscribe(Array.empty)
		  poller.register(sub)
		  pub.sendmsg(outgoingMessage.getBytes, 0)
		  poller.poll must equal(1)
		  poller.pollin(0) must equal(true)
		  
		  val incomingMessage = sub.recvmsg(0)
		  incomingMessage must equal(outgoingMessage.getBytes)
      
		  sub.close
		  pub.close
		  context.term
	  }
	  "support req-rep connection pattern" in {
		  val context = XS.context
		  val (req, rep) = (
    		context.socket(XS.REQ), 
    		context.socket(XS.REP)
    	  )
    	
    	  rep.bind("inproc://xs-rep")
    	  req.connect("inproc://xs-rep")
    	  req.send(outgoingMessage.getBytes, outgoingMessage.length, 0)
    	  val incomingMessage = rep.recv(outgoingMessage.length, 0)
    	  rep.send(responseMessage.getBytes, responseMessage.length, 0)
    	  val endMessage = req.recv(responseMessage.length, 0)
    	  incomingMessage must equal(outgoingMessage.getBytes)
		  endMessage must equal(responseMessage.getBytes)
		  
		  req.close
		  rep.close
		  context.term
	  }
	  "support push-pull connection pattern" in {
		  val context = XS.context
		  val (push, pull) = (
		  	context.socket(XS.PUSH),
		  	context.socket(XS.PULL)
		  )
		  
		  pull.bind("inproc://xs-pull")
		  push.connect("inproc://xs-pull")
		  
		  push.sendmsg(outgoingMessage.getBytes, 0)
		  val incomingMessage = pull.recvmsg(0)
		  incomingMessage must equal(outgoingMessage.getBytes)
		  
		  push.close
		  pull.close
		  context.term
	  }
	  "support Exclusive pair connection pattern" in {
		  val context = XS.context
		  val (peer1, peer2) = (
			context.socket(XS.PAIR),
			context.socket(XS.PAIR)
		  )
		  
		  peer1.bind("inproc://xs-pair")
		  peer2.connect("inproc://xs-pair")
		  
		  peer2.sendmsg(outgoingMessage.getBytes, 0)
		  var incomingMessage = peer1.recvmsg(0)
		  incomingMessage must equal(outgoingMessage.getBytes)
		  
		  peer1.sendmsg(responseMessage.getBytes, 0)
		  incomingMessage = peer2.recvmsg(0)
		  
		  incomingMessage must equal(responseMessage.getBytes)
		  peer2.close
		  peer1.close
		  context.term
	  } 
	  "support surveyor-respondent connection pattern" in {
	  	val context = XS.context
	  	val (surveyor, respondent) = (
	  		context.socket(XS.SURVEYOR),
	  		context.socket(XS.RESPONDENT)
	  	)
	  	
	  	surveyor.bind("inproc://xs-spec")
	  	respondent.connect("inproc://xs-spec")
	  	
	  	surveyor.sendmsg("ABC".getBytes, 0)
	  	var outputMsg = new String(respondent.recvmsg(0))
	  	respondent.sendmsg("DE".getBytes, 0)
	  	outputMsg must equal("ABC")
	  	
	  	outputMsg = new String(surveyor.recvmsg(0))
	  	outputMsg must equal("DE")
	  	
	  	surveyor.close
	  	respondent.close
	  	context.term
	  }
	  "support polling of multiple sockets" in {
		  val context = XS.context
		  val (pub, poller) = (context.socket(XS.PUB), context.poller)
		  pub.bind("inproc://xs-spec")
		  val (sub_x, sub_y) = (connectSubscriber(context), connectSubscriber(context))
		  poller.register(sub_x)
		  poller.register(sub_y)
		  pub.sendmsg(outgoingMessage.getBytes, 0)
		  poller.poll must equal(2)
		  poller.pollin(0) must equal(true)
		  poller.pollin(1) must equal(true)
		  sub_x.close
		  sub_y.close
		  pub.close
		  context.term
	  }
	  "support sending of zero-length messages" in {
		  val context = XS.context
		  val pub = context.socket(XS.PUB)
          pub.sendmsg("".getBytes, 0) must equal(true)
      	  pub.close
      }
	  "support of filtering" in {
	  	val context = XS.context
	  	val sub = context.socket(XS.SUB)
	  	
	  	sub.setFilter(XS_FILTER_TOPIC)
	  	sub.getFilter must equal(XS_FILTER_TOPIC)
	  	
	  	sub.close
	  	context.term
	  }
  	}
  	
  	def connectSubscriber(context: Context) = {
	  val socket = context.socket(XS.SUB) 
    		socket.connect("inproc://xs-spec")
    	socket.subscribe(Array.empty)
    	socket
  	}
  	lazy val outgoingMessage = "hello"
  	lazy val responseMessage = "response"
}
