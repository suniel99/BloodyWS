# DFGGenerator Makefile
# Serguei Mokhov
#
# $Header: /cvsroot/gipsy/gipsy/src/gipsy/GIPC/DFG/DFGGenerator/Makefile,v 1.8 2005/09/11 14:06:54 mokhov Exp $
#

BASE_GIPSY_DIR=../../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/marf.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	DFGCodeGenerator.class \
	DFGSubtree.class \
	DFGSubtreeContents.class \
	DFGTranCodeGenerator.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

DFGCodeGenerator.class: DFGCodeGenerator.java
DFGSubtree.class: DFGSubtree.java
DFGSubtreeContents.class: DFGSubtreeContents.java
DFGTranCodeGenerator.class: DFGTranCodeGenerator.java

clean:
	rm -f $(CLASSFILES)

maintainer-clean: clean
	rm -f *~ *.local *.log

# EOF
