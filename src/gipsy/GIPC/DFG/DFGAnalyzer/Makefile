# DFGAnalyzer Makefile
# Serguei Mokhov
#
# $Id: Makefile,v 1.10 2013/02/23 00:27:25 mokhov Exp $
#

BASE_GIPSY_DIR = ../../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:$(LIB_GIPSY_DIR)/jung-visualization-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-algorithms-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-graph-impl-2.0.1.jar::$(LIB_GIPSY_DIR)/jgoodies/forms-1.3.0.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

JAVACCTMPFILES = \
    Node.java \
    ParseException.java \
    SimpleCharStream.java \
    Token.java \
    TokenMgrError.java

CLASSFILES = \
	DFGAnalyzer.class \
	DFGParser.class \
	DFGParserConstants.class \
	DFGParserTokenManager.class \
	DFGParserTreeConstants.class \
	JJTDFGParserState.class \
	LucidCodeGenerator.class \
	LucidNodeItem.class \
	ParserFacet.class \
	SimpleNode.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

DFGAnalyzer.class: DFGAnalyzer.java
DFGParser.class: DFGParser.java
DFGParserConstants.class: DFGParserConstants.java
DFGParserTokenManager.class: DFGParserTokenManager.java
DFGParserTreeConstants.class: DFGParserTreeConstants.java
JJTDFGParserState.class: JJTDFGParserState.java
LucidCodeGenerator.class: LucidCodeGenerator.java
LucidNodeItem.class: LucidNodeItem.java
ParserFacet.class: ParserFacet.java
SimpleNode.class: SimpleNode.java


javacc: maintainer-clean
	$(BASE_GIPSY_DIR)/tools/javacc/jjtree $(LIB_GIPSY_DIR) DFGParser.jjt && \
    $(BASE_GIPSY_DIR)/tools/javacc/javacc $(LIB_GIPSY_DIR) DFGParser.jj

clean:
	rm -f $(CLASSFILES)

maintainer-clean: clean
	rm -f *~ *.local *.log *.jj $(JAVACCTMPFILES)

# EOF
