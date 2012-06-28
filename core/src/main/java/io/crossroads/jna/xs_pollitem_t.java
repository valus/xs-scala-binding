package io.crossroads.jna;

import com.sun.jna.*;

public class xs_pollitem_t extends Structure {
  public Pointer socket;
  public int fd;
  public short events;
  public short revents;
}
