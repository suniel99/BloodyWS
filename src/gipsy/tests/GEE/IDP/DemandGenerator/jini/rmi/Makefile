# GEE IDP DemandGenerator Jini RMI Testing Modules Makefile
# Serguei Mokhov
#
# $Id: Makefile,v 1.2 2013/02/23 01:51:58 mokhov Exp $
#

VERSION=1.0.0-devel-`date +%Y%m%d`

BASE_GIPSY_DIR=../../../../../../..
include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:$(LIB_GIPSY_DIR)/jung-visualization-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-algorithms-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-graph-impl-2.0.1.jar::$(LIB_GIPSY_DIR)/jgoodies/forms-1.3.0.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	DemandDispatcherClientTest.class \
	DemandSender.class \
	DemandWorker.class \
	JiniDemandDispatcherTest.class \
	JINITAConcurrencyTest.class \
	JiniTransportAgentTest.java

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1


all: $(CLASSFILES)
	@echo "(-: GEE IDP DemandGenerator Jini RMI Test Suite build has been successful :-)"

DemandDispatcherClientTest.class: DemandDispatcherClientTest.java
DemandSender.class: DemandSender.java
DemandWorker.class: DemandWorker.java
JiniDemandDispatcherTest.class: JiniDemandDispatcherTest.java
JINITAConcurrencyTest.class: JINITAConcurrencyTest.java
JiniTransportAgentTest.class: JiniTransportAgentTest.java


clean:
	rm -f *.class

maintainer-clean: clean
	rm -f *.log *.diff *.jar *.gipsy

# EOF
