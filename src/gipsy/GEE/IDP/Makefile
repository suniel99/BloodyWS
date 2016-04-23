# General IDP Makefile
#
# $Id: Makefile,v 1.14 2011/03/12 05:02:42 mokhov Exp $
#

BASE_GIPSY_DIR=../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

COMMON_DG_FILES = \
	DemandMonitor.java \
	LUSException.java \
	DMSException.java \
	ITransportAgent.java

CLASSFILES = \
	LUSException.class \
	DemandMonitor.class \
	DMSException.class \
	ITransportAgent.class

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)
	$(MAKE) -C DemandGenerator all


LUSException.class: LUSException.java
DemandMonitor.class: DemandMonitor.java
DMSException.class: DMSException.java
ITransportAgent.class: ITransportAgent.java

clean:
	rm -f *.class
	$(MAKE) -C DemandGenerator $@
	$(MAKE) -C DemandDispatcher $@
	$(MAKE) -C DemandWorker $@
	$(MAKE) -C DemandStore $@
	$(MAKE) -C demands $@

maintainer-clean: clean
	rm -f *.log *.diff *~
	$(MAKE) -C DemandGenerator $@
	$(MAKE) -C DemandDispatcher $@
	$(MAKE) -C DemandWorker $@
	$(MAKE) -C DemandStore $@
	$(MAKE) -C demands $@

# EOF
