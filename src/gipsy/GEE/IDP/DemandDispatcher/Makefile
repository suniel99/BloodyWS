# DemandDispatcher Makefile
#
# Serguei Mokhov
#
# $Id: Makefile,v 1.2 2011/03/12 05:02:43 mokhov Exp $
#

BASE_GIPSY_DIR=../../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

DEMAND_DISPATCHER_FILES = \
	IDispatcherEntry.java \
	IDemandDispatcher.java \
	DemandDispatcher.java \
	DemandDispatcherException.java

CLASSFILES = \
	IDemandDispatcher.class \
	IDispatcherEntry.class \
	DemandDispatcher.class \
	DemandDispatcherException.class

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

IDemandDispatcher.class: IDemandDispatcher.java
IDispatcherEntry.class: IDispatcherEntry.java
DemandDispatcher.class: DemandDispatcher.java
DemandDispatcherException.class: DemandDispatcherException.java

clean:
	rm -f *.class

maintainer-clean: clean
	rm -f *.log *.diff *~

# EOF
