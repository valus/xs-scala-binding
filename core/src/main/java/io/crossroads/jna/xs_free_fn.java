package io.crossroads.jna;

import com.sun.jna.*;

public interface xs_free_fn extends Callback {

	public void invoke(Pointer data, Pointer memory);
}