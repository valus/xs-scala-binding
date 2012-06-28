package io.crossroads.jna

import com.sun.jna.Callback
import com.sun.jna.Pointer

trait xs_free_fn extends Callback {

	def invoke(data: Pointer, memory: Pointer): Unit
}