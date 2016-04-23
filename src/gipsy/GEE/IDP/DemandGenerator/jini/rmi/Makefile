# GEE IDP DemandGenerator Jini RMI Makefile
#
# $Id: Makefile,v 1.2 2013/02/23 01:51:25 mokhov Exp $
#

BASE_GIPSY_DIR=../../../../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:$(LIB_GIPSY_DIR)/jung-visualization-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-algorithms-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-graph-impl-2.0.1.jar::$(LIB_GIPSY_DIR)/jgoodies/forms-1.3.0.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	DemandDispatcherAgent.class \
	DemandDispatcherClient.class \
	IJiniDemandDispatcher.class \
	IJiniServiceDiscoverer.class \
	IJINITransportAgent.class \
	JavaSpaceAccessObject.class \
	JiniDemandDispatcher.class \
	JiniDispatcherEntry.class \
	JINITA.class \
	JINITransportAgent.class \
	MulticastJiniServiceDiscoverer.class \
	UnicastJiniServiceDiscoverer.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

run:
	@echo "FIXME: Run option ain't here yet :-("

DemandDispatcherAgent.class: DemandDispatcherAgent.java
DemandDispatcherClient.class: DemandDispatcherClient.java
IJiniDemandDispatcher.class: IJiniDemandDispatcher.java
IJiniServiceDiscoverer.class: IJiniServiceDiscoverer.java
IJINITransportAgent.class: IJINITransportAgent.java
JavaSpaceAccessObject.class: JavaSpaceAccessObject.java
JiniDemandDispatcher.class: JiniDemandDispatcher.java
JiniDispatcherEntry.class: JiniDispatcherEntry.java
JINITA.class: JINITA.java
JINITransportAgent.class: JINITransportAgent.java
MulticastJiniServiceDiscoverer.class: MulticastJiniServiceDiscoverer.java
UnicastJiniServiceDiscoverer.class: UnicastJiniServiceDiscoverer.java

clean:
	rm -f $(CLASSFILES) *.class

maintainer-clean: clean
	rm -f *.log *.diff *~

# EOF
