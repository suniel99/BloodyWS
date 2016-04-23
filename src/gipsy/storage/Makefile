# General Storage Makefile
# Serguei Mokhov
#
# $Header: /cvsroot/gipsy/gipsy/src/gipsy/storage/Makefile,v 1.6 2005/09/11 14:06:57 mokhov Exp $
#

BASE_GIPSY_DIR=../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	Dictionary.class \
	DictionaryItem.class \
	FunctionItem.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

Dictionary.class: Dictionary.java
DictionaryItem.class: DictionaryItem.java
FunctionItem.class: FunctionItem.java

clean:
	rm -f *.class

maintainer-clean: clean
	rm -f *.log *.diff

# EOF
