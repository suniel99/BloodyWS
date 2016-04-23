# Communication Procedure Generator Makefile
# Serguei Mokhov
#
# $Header: /cvsroot/gipsy/gipsy/src/gipsy/GIPC/imperative/CommunicationProcedureGenerator/Makefile,v 1.6 2005/09/11 00:20:12 mokhov Exp $
#

BASE_GIPSY_DIR = ../../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/marf.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	CommunicationProcedureGenerator.class \
	NullCommunicationProcedure.class \
	RMICommunicationProcedure.class \
	CORBACommunicationProcedure.class \
	DCOMCommunicationProcedure.class \
	JiniCommunicationProcedure.class \
	CommunicationProcedure.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

CommunicationProcedureGenerator.class: CommunicationProcedureGenerator.java
NullCommunicationProcedure.class: NullCommunicationProcedure.java
RMICommunicationProcedure.class: RMICommunicationProcedure.java
CORBACommunicationProcedure.class: CORBACommunicationProcedure.java
DCOMCommunicationProcedure.class: DCOMCommunicationProcedure.java
JiniCommunicationProcedure.class: JiniCommunicationProcedure.java
CommunicationProcedure.class: CommunicationProcedure.java


clean:
	rm -f $(CLASSFILES)

maintainer-clean: clean
	rm -f *.log *.diff

# EOF
