package io.crossroads.jna

import com.sun.jna.Structure
import com.sun.jna.Pointer

class xs_pollitem_t extends Structure {
  var socket: Pointer = _
  var fd: Int = _
  var events: Short = _
  var revents: Short = _
}