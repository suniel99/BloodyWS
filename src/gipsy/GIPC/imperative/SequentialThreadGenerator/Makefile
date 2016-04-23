# Sequential Thread Generator Makefile
# Serguei Mokhov
#
# $Header: /cvsroot/gipsy/gipsy/src/gipsy/GIPC/imperative/SequentialThreadGenerator/Makefile,v 1.5 2005/09/05 02:54:30 mokhov Exp $
#

BASE_GIPSY_DIR = ../../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	SequentialThreadGenerator.class \
	SequentialThreadSourceGenerator.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

SequentialThreadGenerator.class: SequentialThreadGenerator.java
SequentialThreadSourceGenerator.class: SequentialThreadSourceGenerator.java

clean:
	rm -f $(CLASSFILES)

maintainer-clean: clean
	rm -f *.log *.diff

# EOF
