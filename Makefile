
OBJDIR = target/obj
SODIR = core/src/main/resources/
SRCDIR = core/src/main/c/jni
CC = gcc -std=gnu99
CCDEPMODE = depmode=gcc3
CFLAGS = -g -O2 -D_THREAD_SAFE -pthread
CPP = gcc -std=gnu99 -E
CPPFLAGS = -Wall -Wno-uninitialized 
CXX = g++
CXXCPP = g++ -E
CXXDEPMODE = depmode=gcc3
CXXFLAGS = -g -O2 -D_THREAD_SAFE -pthread
CYGPATH_W = echo
DEFS = -DHAVE_CONFIG_H

INCLUDE = -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux \
	-I${JAVA_HOME}/../include -I${JAVA_HOME}/../include/linux \
	-I/System/Library/Frameworks/JavaVM.framework/Headers \
	-I/opt/local/include

C_OBJ = $(OBJDIR)/sxs.o
C_SRC = $(SRCDIR)/sxs.c
C_SO = $(SODIR)/sxs.so

.PHONY: all
all: $(C_SO)

$(C_SO): $(C_OBJ) $(SODIR)
	cc $(DEFS) $(CFLAGS) $(CPPFLAGS) -fPIC -shared -o $@ $(C_OBJ) /opt/local/lib/libxs.dylib
$(C_OBJ): $(C_SRC) $(HEADERS) $(OBJDIR)
	cc $(DEFS) $(CFLAGS) $(CPPFLAGS) -fPIC -c -O -o $@ $(INCLUDE) $(C_SRC)

$(OBJDIR):
	mkdir $(OBJDIR)
$(SODIR):
	mkdir $(SODIR)

clean:
	rm -rf $(C_OBJ) $(C_SO)