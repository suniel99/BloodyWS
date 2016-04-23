# General GarbageCollector Makefile
# Serguei Mokhov
#
# $Id: Makefile,v 1.3 2011/03/12 05:07:58 mokhov Exp $
#

BASE_GIPSY_DIR=../../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/netcdfAll.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	CopyingGC.class \
	GCFactory.class \
	GarbageCollector.class \
	MarkSweepGC.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

CopyingGC.class: CopyingGC.java
GCFactory.class: GCFactory.java
GarbageCollector.class: GarbageCollector.java
MarkSweepGC.class: MarkSweepGC.java

clean:
	rm -f $(CLASSFILES)

maintainer-clean: clean
	rm -f *.diff *.log *~

# EOF
