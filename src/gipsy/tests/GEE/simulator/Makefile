# GEE Simulator Testing Modules Makefile
# Serguei Mokhov
#
# $Id: Makefile,v 1.2 2013/02/23 01:52:15 mokhov Exp $
#

VERSION=1.0.0-devel-`date +%Y%m%d`

BASE_GIPSY_DIR=../../../..
include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:$(LIB_GIPSY_DIR)/jung-visualization-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-algorithms-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-graph-impl-2.0.1.jar::$(LIB_GIPSY_DIR)/jgoodies/forms-1.3.0.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	DemandClassPool.class \
	DemandFactory.class \
	DemandIDPool.class \
	DemandPool.class \
	DemandResponseTimeTester.class \
	DemandSender.class \
	DGTDialog.class \
	DGT.class \
	DGTSimulator.class \
	DSTSpaceScalabilityTester.class \
	GlobalDef.class \
	GUIThread.class \
	ProfileDialog.class \
	ProfileFilter.class \
	ResultAnalyst.class \
	ResultPool.class \
	ResultProcessor.class \
	ResultReceiver.class \
	Semaphore.class \
	SpaceTimeTester.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1


all: $(CLASSFILES)
	$(MAKE) -C demands $@
	$(MAKE) -C jini $@
	@echo "(-: GEE Simulator Test Suite build has been successful :-)"

DemandClassPool.class: DemandClassPool.java
DemandFactory.class: DemandFactory.java
DemandIDPool.class: DemandIDPool.java
DemandPool.class: DemandPool.java
DemandResponseTimeTester.class: DemandResponseTimeTester.java
DemandSender.class: DemandSender.java
DGTDialog.class: DGTDialog.java
DGT.class: DGT.java
DGTSimulator.class: DGTSimulator.java
DSTSpaceScalabilityTester.class: DSTSpaceScalabilityTester.java
GlobalDef.class: GlobalDef.java
GUIThread.class: GUIThread.java
ProfileDialog.class: ProfileDialog.java
ProfileFilter.class: ProfileFilter.java
ResultAnalyst.class: ResultAnalyst.java
ResultPool.class: ResultPool.java
ResultProcessor.class: ResultProcessor.java
ResultReceiver.class: ResultReceiver.java
Semaphore.class: Semaphore.java
SpaceTimeTester.class: SpaceTimeTester.java


clean:
	rm -f *.class
	$(MAKE) -C demands $@
	$(MAKE) -C jini $@

maintainer-clean: clean
	rm -f *.log *.diff *.jar *.gipsy *~
	$(MAKE) -C demands $@
	$(MAKE) -C jini $@

# EOF
