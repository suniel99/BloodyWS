# GEE Simulator Demands Testing Modules Makefile
# Serguei Mokhov
#
# $Id: Makefile,v 1.1 2011/02/06 03:46:12 mokhov Exp $
#

VERSION=1.0.0-devel-`date +%Y%m%d`

BASE_GIPSY_DIR=../../../../..
include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	LightUniqueDemand.class \
	Pi.class \
	PrintScreen.class \
	SerializedImage.class \
	SizeAdjustableDemand.class \
	WorkDemandHD.class \
	WorkDemandPi.class \
	WorkDemandScrSht.class \
	WorkResultHD.class \
	WorkResultPi.class \
	WorkResultScrSht.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1


all: $(CLASSFILES)
	@echo "(-: GEE Simulator Demands Test Suite build has been successful :-)"

LightUniqueDemand.class: LightUniqueDemand.java
Pi.class: Pi.java
PrintScreen.class: PrintScreen.java
SerializedImage.class: SerializedImage.java
SizeAdjustableDemand.class: SizeAdjustableDemand.java
WorkDemandHD.class: WorkDemandHD.java
WorkDemandPi.class: WorkDemandPi.java
WorkDemandScrSht.class: WorkDemandScrSht.java
WorkResultHD.class: WorkResultHD.java
WorkResultPi.class: WorkResultPi.java
WorkResultScrSht.class: WorkResultScrSht.java


clean:
	rm -f *.class

maintainer-clean: clean
	rm -f *.log *.diff *.jar *.gipsy *~

# EOF
