# GEE Multitier GMT Testing Modules Makefile
# Serguei Mokhov
#
# $Id: Makefile,v 1.1 2011/02/06 03:26:50 mokhov Exp $
#

VERSION=1.0.0-devel-`date +%Y%m%d`

BASE_GIPSY_DIR=../../../../..
include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:$(LIB_GIPSY_DIR)/jung-visualization-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-algorithms-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-graph-impl-2.0.1.jar:$(LIB_GIPSY_DIR)/jgoodies/forms-1.3.0.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	GMTTestConsole.class \
	GMTCommand.class \
	GMTCommandAllocate.class \
	GMTCommandDeallocate.class \
	GMTCommandSimpleFactory.class \
	GMTCommandParser.class \
	GMTd.class  \
	ICommand.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1


all: $(CLASSFILES)
	@echo "(-: GEE Multitier GMT Test Suite build has been successful :-)"

GMTTestConsole.class: GMTTestConsole.java
GMTCommand.class: GMTCommand.java
GMTCommandAllocate.class: GMTCommandAllocate.java
GMTCommandDeallocate.class: GMTCommandDeallocate.java
GMTCommandSimpleFactory.class: GMTCommandSimpleFactory.java
GMTCommandParser.class: GMTCommandParser.java
GMTd.class: GMTd.java
ICommand.class: ICommand.java



clean:
	rm -f *.class

maintainer-clean: clean
	rm -f *.log *.diff *.jar *.gipsy *~

# EOF
