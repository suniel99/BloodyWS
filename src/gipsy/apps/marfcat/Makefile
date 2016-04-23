#
# Makfile for the 'gipsy.apps.marfcat' package.
#
# $Id: Makefile,v 1.2 2013/02/23 01:51:36 mokhov Exp $
#

BASE_GIPSY_DIR=../../..

include $(BASE_GIPSY_DIR)/../Makefile.global


CLASSFILES = \
	IMARFCATDelegateApp.class \
	MARFCATDGT.class \
	MARFCATDWT.class \
	MARFPCATDGT.class \
	MARFPCATDWT.class

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:$(LIB_GIPSY_DIR)/jung-visualization-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-algorithms-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-graph-impl-2.0.1.jar:$(LIB_GIPSY_DIR)/jgoodies/forms-1.3.0.jar:$(LIB_GIPSY_DIR)/marfcat.jar:$(LIB_GIPSY_DIR)/commons/commons-lang3-3.4.jar:$(LIB_GIPSY_DIR)/joda-time/joda-time-2.4.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

IMARFCATDelegateApp.class: IMARFCATDelegateApp.java
MARFCATDGT.class: MARFCATDGT.java
MARFCATDWT.class: MARFCATDWT.java
MARFPCATDGT.class: MARFPCATDGT.java
MARFPCATDWT.class: MARFPCATDWT.java


clean:
	rm -f *.class

maintainer-clean: clean
	rm -f *.log *.diff *~

# EOF
