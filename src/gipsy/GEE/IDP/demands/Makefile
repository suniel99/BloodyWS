# General Demands Makefile
#
# $Id: Makefile,v 1.2 2011/03/12 05:02:46 mokhov Exp $
#

BASE_GIPSY_DIR=../../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

CLASSFILES = \
	Demand.class \
	DemandSignature.class \
	DemandState.class \
	DemandType.class \
	IDemand.class \
	IDemandList.class \
	IntensionalDemand.class \
	ProceduralDemand.class \
	ResourceDemand.class \
	SystemDemand.class \
	TimeLine.class

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

Demand.class: Demand.java
DemandSignature.class: DemandSignature.java
DemandState.class: DemandState.java
DemandType.class: DemandType.java
IDemand.class: IDemand.java
IDemandList.class: IDemandList.java
IntensionalDemand.class: IntensionalDemand.java
ProceduralDemand.class: ProceduralDemand.java
ResourceDemand.class: ResourceDemand.java
SystemDemand.class: SystemDemand.java
TimeLine.class: TimeLine.java

clean:
	rm -f *.class

maintainer-clean: clean
	rm -f *.log *.diff *~

# EOF
