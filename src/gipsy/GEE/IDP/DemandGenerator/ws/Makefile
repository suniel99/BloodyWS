# GEE IDP DemandGenerator ws Makefile
#
# $Id: Makefile,v 1.8 2013/02/23 01:51:31 mokhov Exp $
#

BASE_GIPSY_DIR=../../../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:$(LIB_GIPSY_DIR)/jung-visualization-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-algorithms-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-graph-impl-2.0.1.jar::$(LIB_GIPSY_DIR)/jgoodies/forms-1.3.0.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	DemandList.class \
	IdentifierContext.class \
	IdentifierContextClient.class \
	IdentifierContextServer.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

run:
	@echo "FIXME: Run option ain't here yet :-("

DemandList.class: DemandList.java
IdentifierContext.class: IdentifierContext.java
IdentifierContextClient.class: IdentifierContextClient.java
IdentifierContextServer.class: IdentifierContextServer.java

clean:
	rm -f $(CLASSFILES)

maintainer-clean: clean
	rm -f *.log *.diff *~

# EOF
