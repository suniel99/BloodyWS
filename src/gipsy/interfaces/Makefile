# General Interfaces Makefile
# Serguei Mokhov
#
# $Header: /cvsroot/gipsy/gipsy/src/gipsy/interfaces/Makefile,v 1.7 2011/01/08 01:49:39 mokhov Exp $
#

BASE_GIPSY_DIR=../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:$(LIB_GIPSY_DIR)/servlet-api.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	AbstractSyntaxTree.class \
	CommunicationException.class \
	CommunicationStatus.class \
	DataFlowGraph.class \
	DemandPropagationResource.class \
	GIPSYProgram.class \
	ICommunicationProcedure.class \
	ICommunicationProceduresEnum.class \
	IIdentifierContext.class \
	ISequentialThread.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

AbstractSyntaxTree.class: AbstractSyntaxTree.java
CommunicationException.class: CommunicationException.java
CommunicationStatus.class: CommunicationStatus.java
DataFlowGraph.class: DataFlowGraph.java
DemandPropagationResource.class: DemandPropagationResource.java
GIPSYProgram.class: GIPSYProgram.java
ICommunicationProcedure.class: ICommunicationProcedure.java
ICommunicationProceduresEnum.class: ICommunicationProceduresEnum.java
IIdentifierContext.class: IIdentifierContext.java
ISequentialThread.class: ISequentialThread.java

clean:
	rm -f *.class

maintainer-clean: clean
	rm -f *.log *.diff

# EOF
