# Java Compiler Makefile
# Serguei Mokhov
#
# $Header: /cvsroot/gipsy/gipsy/src/gipsy/GIPC/imperative/Java/Makefile,v 1.5 2005/09/14 00:14:06 mokhov Exp $
#

BASE_GIPSY_DIR = ../../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/marf.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	JavaCompiler.class \
	JavaSequentialThreadGenerator.class \
	JavaCommunicationProcedureGenerator.class \
	TypeMap.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

JavaCompiler.class: JavaCompiler.java
JavaSequentialThreadGenerator.class: JavaSequentialThreadGenerator.java
JavaCommunicationProcedureGenerator.class: JavaCommunicationProcedureGenerator.java
TypeMap.class: TypeMap.java

clean:
	rm -f $(CLASSFILES) *.class

maintainer-clean: clean
	rm -f *.log *.diff

# EOF
