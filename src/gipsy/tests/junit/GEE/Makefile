# JUnit GEE Testing Modules Makefile
# Serguei Mokhov
#
# $Id: Makefile,v 1.1 2011/02/05 07:55:47 mokhov Exp $
#

VERSION=1.0.0-devel-`date +%Y%m%d`

BASE_GIPSY_DIR=../../../..
include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1


all: $(CLASSFILES)
	$(MAKE) -C multitier $@
	@echo "(-: JUnit GEE Test Suite build has been successful :-)"

GIPSYContextTest.class: GIPSYContextTest.java

clean:
	rm -f *.class
	$(MAKE) -C multitier $@

maintainer-clean: clean
	rm -f *.log *.diff *.jar *.gipsy
	$(MAKE) -C multitier $@

# EOF
