# Main Analyzers Makefile
# Serguei Mokhov
#
# $Header: /cvsroot/gipsy/gipsy/src/gipsy/GIPC/analysis/Makefile,v 1.1 2011/01/06 00:20:42 mokhov Exp $
#

BASE_GIPSY_DIR = ../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/marf.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	IAnalyzer.class \
	RankAnalyzer.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

IAnalyzer.class: IAnalyzer.java
RankAnalyzer.class: RankAnalyzer.java

clean:
	rm -f $(CLASSFILES)

maintainer-clean: clean
	rm -f *.log *.diff

# EOF
