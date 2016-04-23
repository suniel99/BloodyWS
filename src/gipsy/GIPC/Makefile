# This is a quick Makefile to make this module compile.
# If this quick hackery is not updated, person to blame: Serguei
#
# $Id: Makefile,v 1.29 2013/02/23 00:48:47 mokhov Exp $
#

VERSION=1.0.0-devel-`date +%Y%m%d`

BASE_GIPSY_DIR = ../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:$(LIB_GIPSY_DIR)/jung-visualization-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-algorithms-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-graph-impl-2.0.1.jar::$(LIB_GIPSY_DIR)/jgoodies/forms-1.3.0.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	GEERGenerator.class \
	GIPC.class \
	GIPCException.class \
	ICompiler.class \
	IdentifierContextCodeGenerator.class \
	SemanticAnalyzer.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1


#
# Java Doc for API doc generation
#

JAVADOC = javadoc
JAVADOCDIR = ../..
JAVADOCFLAGS = -verbose -use -version -author \
               -stylesheetfile ../doc/src/sgml/stylesheet.css

all: $(CLASSFILES) copyright 
	$(MAKE) -C DFG $@
	$(MAKE) -C imperative $@
	$(MAKE) -C intensional $@
	$(MAKE) -C Preprocessing $@
	$(MAKE) -C util $@
	( \
		cd ../.. && \
		ln -f -s ../COPYRIGHT COPYRIGHT && \
		rm -f gipsy/GIPC/gipc.jar && \
		jar cvmf gipsy/GIPC/manifest.mf gipsy/GIPC/gipc-$(VERSION).jar \
			COPYRIGHT \
			gipsy/GIPC \
			gipsy/GEE \
			gipsy/interfaces \
			gipsy/lang \
			gipsy/storage \
			gipsy/util \
			gipsy/wrappers \
	)
	ln -s -f gipc-$(VERSION).jar gipc.jar
	@echo "(-: GIPC build has been successful :-)"

run:
	@echo "FIXME: Run option ain't here yet :-(, but you may try 'make run-gipc'"

run-gipc: all
#	java -classpath $(BASE_GIPSY_DIR):$(CLASSPATH) gipsy.GIPC.GIPC --debug ../tests/gipsy/ret-object1.ipl
	java -classpath $(BASE_GIPSY_DIR):$(CLASSPATH) gipsy.GIPC.GIPC --debug --gee a.ipl

GIPC.class: GIPC.java
GEERGenerator.class: GEERGenerator.java
GIPCException.class: GIPCException.java
ICompiler.class: ICompiler.java
IdentifierContextCodeGenerator.class: IdentifierContextCodeGenerator.java
SemanticAnalyzer.class: SemanticAnalyzer.java

prep-test:
	make -C Preprocessing javacc all
	make run-gipc

copyright: ../../../COPYRIGHT
	ln -f -s ../../../COPYRIGHT COPYRIGHT

clean:
	rm -f $(CLASSFILES)
	$(MAKE) -C DFG $@
	$(MAKE) -C imperative $@
	$(MAKE) -C intensional $@
	$(MAKE) -C Preprocessing $@
	$(MAKE) -C util $@

maintainer-clean: clean
	rm -f *.log *.diff *.jar COPYRIGHT *~
	$(MAKE) -C DFG $@
	$(MAKE) -C imperative $@
	$(MAKE) -C intensional $@
	$(MAKE) -C Preprocessing $@
	$(MAKE) -C util $@

# EOF
