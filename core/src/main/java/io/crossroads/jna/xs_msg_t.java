package io.crossroads.jna;

import com.sun.jna.*;

public class xs_msg_t extends Structure {
	public Pointer content;
	public byte flags;
	public byte vsm_size;
	public byte[] vsm_data = new byte[XS_MAX_VSM_SIZE];
	private static final int XS_MAX_VSM_SIZE = CrossroadsIO.XS_MAX_VSM_SIZE();
}