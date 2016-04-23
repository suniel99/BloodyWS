# GIPSY Language Makefile
# Serguei Mokhov
#
# $Header: /cvsroot/gipsy/gipsy/src/gipsy/lang/Makefile,v 1.3 2005/09/11 14:06:56 mokhov Exp $
#

BASE_GIPSY_DIR=../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	GIPSYArray.class \
	GIPSYBoolean.class \
	GIPSYCharacter.class \
	GIPSYDouble.class \
	GIPSYEmbed.class \
	GIPSYFloat.class \
	GIPSYIdentifier.class \
	GIPSYInteger.class \
	GIPSYObject.class \
	GIPSYString.class \
	GIPSYType.class \
	GIPSYVoid.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

GIPSYArray.class: GIPSYArray.java
GIPSYBoolean.class: GIPSYBoolean.java
GIPSYCharacter.class: GIPSYCharacter.java
GIPSYDouble.class: GIPSYDouble.java
GIPSYEmbed.class: GIPSYEmbed.java
GIPSYFloat.class: GIPSYFloat.java
GIPSYIdentifier.class: GIPSYIdentifier.java
GIPSYInteger.class: GIPSYInteger.java
GIPSYObject.class: GIPSYObject.java
GIPSYString.class: GIPSYString.java
GIPSYType.class: GIPSYType.java
GIPSYVoid.class: GIPSYVoid.java

clean:
	rm -f *.class

maintainer-clean: clean
	rm -f *.log *.diff

# EOF
