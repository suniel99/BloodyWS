# General GEE Multitier GMT Demands Makefile
#
# $Id: Makefile,v 1.1 2011/02/24 00:38:48 mokhov Exp $
#

BASE_GIPSY_DIR=../../../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

CLASSFILES = \
	DGTRegistration.class \
	DSTIssueReport.class \
	DSTRegistration.class \
	DWTRegistration.class \
	NodeRegistration.class \
	RegistrationResult.class \
	TierAllocationRequest.class \
	TierAllocationResult.class \
	TierDeallocationRequest.class \
	TierDeallocationResult.class \
	TierRegistration.class \
	TierRegistrationResult.class

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:$(LIB_GIPSY_DIR)/tools.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

DGTRegistration.class: DGTRegistration.java
DSTIssueReport.class: DSTIssueReport.java
DSTRegistration.class: DSTRegistration.java
DWTRegistration.class: DWTRegistration.java
NodeRegistration.class: NodeRegistration.java
RegistrationResult.class: RegistrationResult.java
TierAllocationRequest.class: TierAllocationRequest.java
TierAllocationResult.class: TierAllocationResult.java
TierDeallocationRequest.class: TierDeallocationRequest.java
TierDeallocationResult.class: TierDeallocationResult.java
TierRegistration.class: TierRegistration.java
TierRegistrationResult.class: TierRegistrationResult.java

clean:
	rm -f *.class

maintainer-clean: clean
	rm -f *.log *.diff *~

# EOF
