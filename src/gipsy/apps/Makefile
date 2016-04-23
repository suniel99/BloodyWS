# Makfile for 'gipsy.apps' and its sub-modules
#
# Serguei Mokhov
#
# $Id: Makefile,v 1.15 2013/02/23 01:51:19 mokhov Exp $
#

VERSION=1.0.0-devel-`date +%Y%m%d`

BASE_GIPSY_DIR = ../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:$(LIB_GIPSY_DIR)/jung-visualization-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-algorithms-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-graph-impl-2.0.1.jar:$(LIB_GIPSY_DIR)/jgoodies/forms-1.3.0.jar:$(LIB_GIPSY_DIR)/marfcat.jar:$(LIB_GIPSY_DIR)/commons/commons-lang3-3.4.jar:$(LIB_GIPSY_DIR)/joda-time/joda-time-2.4.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1


all: $(CLASSFILES)
	$(MAKE) -C marfcat $@
	
clean:
	rm -f *.class
	$(MAKE) -C marfcat $@

maintainer-clean: clean
	rm -f *.log *.diff *.jar *~
	$(MAKE) -C marfcat $@

# EOF
