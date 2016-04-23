# Demand Store Makefile
#
# $Header: /cvsroot/gipsy/gipsy/src/gipsy/GEE/IDP/DemandStore/Makefile,v 1.1 2011/01/06 03:03:49 mokhov Exp $
#

BASE_GIPSY_DIR=../../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

CLASSFILES =

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

clean:
	rm -f *.class

maintainer-clean: clean
	rm -f *.log *.diff

# EOF
