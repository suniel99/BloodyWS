# General GEE IDP DemandGenerator Makefile
# Serguei Mokhov
#
# $Id: Makefile,v 1.11 2013/02/23 01:51:23 mokhov Exp $
#

BASE_GIPSY_DIR=../../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib

CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:$(LIB_GIPSY_DIR)/jung-visualization-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-algorithms-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-graph-impl-2.0.1.jar::$(LIB_GIPSY_DIR)/jgoodies/forms-1.3.0.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	DemandGenerator.class \
	IDemandGenerator.class \
	Interpreter.class \
	LegacyEductiveInterpreter.class \
	LegacyInterpreter.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)
	$(MAKE) -C jini $@
	$(MAKE) -C jms $@
	$(MAKE) -C rmi $@
	$(MAKE) -C threaded $@

DemandGenerator.class: DemandGenerator.java
IDemandGenerator.class: IDemandGenerator.java
Interpreter.class: Interpreter.java
LegacyEductiveInterpreter.class: LegacyEductiveInterpreter.java
LegacyInterpreter.class: LegacyInterpreter.java

clean:
	rm -f $(CLASSFILES)
	$(MAKE) -C jini $@
	$(MAKE) -C jms $@
	$(MAKE) -C rmi $@
	$(MAKE) -C threaded $@

maintainer-clean: clean
	rm -f *.diff *.log *~
	$(MAKE) -C jini $@
	$(MAKE) -C jms $@
	$(MAKE) -C rmi $@
	$(MAKE) -C threaded $@

# EOF
