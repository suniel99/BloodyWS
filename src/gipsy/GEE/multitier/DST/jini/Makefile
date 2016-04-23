# General GEE Multitier DST Jini Makefile
#
# $Id: Makefile,v 1.2 2012/06/26 03:42:36 mokhov Exp $
#

BASE_GIPSY_DIR=../../../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

CLASSFILES = \
	BoundedServerSocketFactory.class \
	BoundedServerSocket.class \
	BoundedSocket.class \
	JiniDSTWrapper.class \
	JiniERIDSTWrapper.class \
	PingSignal.class

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:$(LIB_GIPSY_DIR)/tools.jar:$(LIB_GIPSY_DIR)/jung-visualization-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-algorithms-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-graph-impl-2.0.1.jar::$(LIB_GIPSY_DIR)/jgoodies/forms-1.3.0.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

BoundedServerSocketFactory.class: BoundedServerSocketFactory.java
BoundedServerSocket.class: BoundedServerSocket.java
BoundedSocket.class: BoundedSocket.java
JiniDSTWrapper.class: JiniDSTWrapper.java
JiniERIDSTWrapper.class: JiniERIDSTWrapper.java
PingSignal.class: PingSignal.java

clean:
	rm -f *.class

maintainer-clean: clean
	rm -f *.log *.diff *~

# EOF
