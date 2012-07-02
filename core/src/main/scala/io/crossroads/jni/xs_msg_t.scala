package io.crossroads.jni

class xs_msg_t {

	var content: Long = 0
	var flags: Byte = 0
	var vsm_size: Byte = 0
	var vsm_data: Array[Byte] = new Array[Byte](XS_MAX_VSM_SIZE)
	val XS_MAX_VSM_SIZE: Int = XSLibrary.XS_MAX_VSM_SIZE
}