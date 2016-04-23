# General Utils Makefile
# Serguei Mokhov
#
# $Header: /cvsroot/gipsy/gipsy/src/gipsy/util/Makefile,v 1.5 2012/06/19 16:58:06 mokhov Exp $
#

BASE_GIPSY_DIR=../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	GIPSYException.class \
	LinkedList.class \
	LinkedListNode.class \
	NotImplementedException.class \
	Upgrade.class \
	DateTimeUtils.class \
	GIPSYRuntimeException.class \
	NetUtils.class \
	Platform.class \
	StringInputStream.class \
	Trace.class


.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

GIPSYException.class: GIPSYException.java
LinkedList.class: LinkedList.java
LinkedListNode.class: LinkedListNode.java
NotImplementedException.class: NotImplementedException.java
Upgrade.class: Upgrade.java
DateTimeUtils.class: DateTimeUtils.java
GIPSYRuntimeException.class: GIPSYRuntimeException.java
NetUtils.class: NetUtils.java
Platform.class: Platform.java
StringInputStream.class: StringInputStream.java
Trace.class: Trace.java

clean:
	rm -f *.class

maintainer-clean: clean
	rm -f *.log *.diff

# EOF
