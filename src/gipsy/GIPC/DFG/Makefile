# Main DFG Makefile
# Serguei Mokhov
#
# $Header: /cvsroot/gipsy/gipsy/src/gipsy/GIPC/DFG/Makefile,v 1.3 2005/09/05 02:54:21 mokhov Exp $
#

BASE_GIPSY_DIR = ../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/marf.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	DFGException.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)
	$(MAKE) -C DFGAnalyzer $@
	$(MAKE) -C DFGGenerator $@

run:
	$(MAKE) -C DFGAnalyzer $@
	$(MAKE) -C DFGGenerator $@

clean:
	rm -f $(CLASSFILES)
	$(MAKE) -C DFGAnalyzer $@
	$(MAKE) -C DFGGenerator $@

maintainer-clean: clean
	rm -f *.log *.diff
	$(MAKE) -C DFGAnalyzer $@
	$(MAKE) -C DFGGenerator $@

# EOF
