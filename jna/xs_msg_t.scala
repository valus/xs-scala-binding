package io.crossroads.jna

import com.sun.jna.Structure
import com.sun.jna.Pointer

class xs_msg_t extends Structure {
	var content: Pointer = _
	var flags: Byte = _
	var vsm_size: Byte = _
	var vsm_data: Array[Byte] = new Array[Byte](XS_MAX_VSM_SIZE)
	val XS_MAX_VSM_SIZE = CrossroadsIO.XS_MAX_VSM_SIZE
}