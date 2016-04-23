# Indexical Lucid Makefile
#
# $Header: /cvsroot/gipsy/gipsy/src/gipsy/GIPC/intensional/SIPL/IndexicalLucid/Makefile,v 1.10 2013/02/23 00:43:42 mokhov Exp $
#

BASE_GIPSY_DIR = ../../../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:$(LIB_GIPSY_DIR)/jung-visualization-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-algorithms-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-graph-impl-2.0.1.jar::$(LIB_GIPSY_DIR)/jgoodies/forms-1.3.0.jar:.

JAVACCTMPFILES = \
    Node.java \
    ParseException.java \
    SimpleCharStream.java \
    SimpleNode.java \
    Token.java \
    TokenMgrError.java

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	IndexicalLucidCompiler.class \
	IndexicalLucidParser.class \
	IndexicalLucidParserConstants.class \
	IndexicalLucidParserTokenManager.class \
	IndexicalLucidParserTreeConstants.class \
	JJTIndexicalLucidParserState.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

javacc: maintainer-clean
	ln -s ../../SimpleNode.java SimpleNode.java
	$(BASE_GIPSY_DIR)/tools/javacc/jjtree $(LIB_GIPSY_DIR) IndexicalLucid.jjt && \
    $(BASE_GIPSY_DIR)/tools/javacc/javacc $(LIB_GIPSY_DIR) IndexicalLucid.jj
	make maintainer-clean

IndexicalLucidCompiler.class: IndexicalLucidCompiler.java
IndexicalLucidParser.class: IndexicalLucidParser.java
IndexicalLucidParserConstants.class: IndexicalLucidParserConstants.java
IndexicalLucidParserTokenManager.class: IndexicalLucidParserTokenManager.java
IndexicalLucidParserTreeConstants.class: IndexicalLucidParserTreeConstants.java
JJTIndexicalLucidParserState.class: JJTIndexicalLucidParserState.java

clean:
	rm -f $(CLASSFILES) *.class

maintainer-clean: clean
	rm -f *.log *.diff *.jj $(JAVACCTMPFILES)

# EOF
