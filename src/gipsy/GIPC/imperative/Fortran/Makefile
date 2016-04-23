# Fortran Compiler Makefile
# Serguei Mokhov
#
# $Header: /cvsroot/gipsy/gipsy/src/gipsy/GIPC/imperative/Fortran/Makefile,v 1.2 2005/09/05 02:54:27 mokhov Exp $
#

BASE_GIPSY_DIR = ../../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/marf.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	FortranCompiler.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

FortranCompiler.class: FortranCompiler.java

clean:
	rm -f $(CLASSFILES)

maintainer-clean: clean
	rm -f *.log *.diff

# EOF
