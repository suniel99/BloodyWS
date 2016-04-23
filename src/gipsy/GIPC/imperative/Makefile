# This is a quick makefile to make this module compile
# If this quick hackery is not updated, person to blame: Serguei
#
# $Id: Makefile,v 1.11 2013/02/23 00:40:08 mokhov Exp $
#

BASE_GIPSY_DIR = ../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:$(LIB_GIPSY_DIR)/jung-visualization-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-algorithms-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-graph-impl-2.0.1.jar::$(LIB_GIPSY_DIR)/jgoodies/forms-1.3.0.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	EImperativeLanguages.class \
	FormatTag.class \
	IImperativeCompiler.class \
	ImperativeCompiler.class \
	ImperativeCompilerException.class \
	ImperativeNode.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)
	$(MAKE) -C SequentialThreadGenerator $@
	$(MAKE) -C CommunicationProcedureGenerator $@
	$(MAKE) -C C $@
	$(MAKE) -C Cpp $@
	$(MAKE) -C Java $@
	$(MAKE) -C Perl $@
	$(MAKE) -C Python $@

EImperativeLanguages.class: EImperativeLanguages.java
FormatTag.class: FormatTag.java
IImperativeCompiler.class: IImperativeCompiler.java
ImperativeCompiler.class: ImperativeCompiler.java
ImperativeCompilerException.class: ImperativeCompilerException.java
ImperativeNode.class: ImperativeNode.java

run:
	@echo "FIXME: Run option ain't here yet :-("

clean:
	rm -f $(CLASSFILES)
	$(MAKE) -C SequentialThreadGenerator $@
	$(MAKE) -C CommunicationProcedureGenerator $@
	$(MAKE) -C C $@
	$(MAKE) -C Cpp $@
	$(MAKE) -C Java $@
	$(MAKE) -C Perl $@
	$(MAKE) -C Python $@

maintainer-clean: clean
	rm -f *.log *.diff *~
	$(MAKE) -C SequentialThreadGenerator $@
	$(MAKE) -C CommunicationProcedureGenerator $@
	$(MAKE) -C C $@
	$(MAKE) -C Cpp $@
	$(MAKE) -C Java $@
	$(MAKE) -C Perl $@
	$(MAKE) -C Python $@

# EOF
