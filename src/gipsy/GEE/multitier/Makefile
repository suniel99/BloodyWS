# General GEE Multitier Makefile
#
# $Id: Makefile,v 1.2 2013/02/23 01:51:39 mokhov Exp $
#

BASE_GIPSY_DIR=../../..

include $(BASE_GIPSY_DIR)/../Makefile.global


CLASSFILES = \
	EDMFImplementation.class \
	GenericTierWrapper.class \
	GIPSYNode.class \
	IMultiTierWrapper.class \
	INodeController.class \
	MultiTierException.class \
	NodeController.class \
	TAExceptionHandler.class \
	TierFactory.class \
	TierIdentity.class \
	TierWrapperFactory.class

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:$(LIB_GIPSY_DIR)/jung-visualization-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-algorithms-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-graph-impl-2.0.1.jar::$(LIB_GIPSY_DIR)/jgoodies/forms-1.3.0.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)
	$(MAKE) -C DGT $@
	$(MAKE) -C DST $@
	$(MAKE) -C DWT $@
	$(MAKE) -C GMT $@

EDMFImplementation.class: EDMFImplementation.java
GenericTierWrapper.class: GenericTierWrapper.java
GIPSYNode.class: GIPSYNode.java
IMultiTierWrapper.class: IMultiTierWrapper.java
INodeController.class: INodeController.java
MultiTierException.class: MultiTierException.java
NodeController.class: NodeController.java
TAExceptionHandler.class: TAExceptionHandler.java
TierFactory.class: TierFactory.java
TierIdentity.class: TierIdentity.java
TierWrapperFactory.class: TierWrapperFactory.java

clean:
	rm -f *.class
	$(MAKE) -C DGT $@
	$(MAKE) -C DST $@
	$(MAKE) -C DWT $@
	$(MAKE) -C GMT $@

maintainer-clean: clean
	rm -f *.log *.diff *~
	$(MAKE) -C DGT $@
	$(MAKE) -C DST $@
	$(MAKE) -C DWT $@
	$(MAKE) -C GMT $@

# EOF
