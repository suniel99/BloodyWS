# GIPL Package Makefile
# Serguei Mokhov
#
# $Header: /cvsroot/gipsy/gipsy/src/gipsy/GIPC/intensional/GIPL/Makefile,v 1.9 2005/09/05 02:54:32 mokhov Exp $
#

BASE_GIPSY_DIR = ../../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/marf.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	GIPLCompiler.class \
	GIPLParser.class \
	GIPLParserConstants.class \
	GIPLParserTokenManager.class \
	GIPLParserTreeConstants.class \
	JJTGIPLParserState.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

JAVACCTMPFILES = \
    Node.java \
    ParseException.java \
    SimpleCharStream.java \
    SimpleNode.java \
    Token.java \
    TokenMgrError.java

all: $(CLASSFILES)

GIPLCompiler.class: GIPLCompiler.java
GIPLParser.class: GIPLParser.java
GIPLParserConstants.class: GIPLParserConstants.java
GIPLParserTokenManager.class: GIPLParserTokenManager.java
GIPLParserTreeConstants.class: GIPLParserTreeConstants.java
JJTGIPLParserState.class: JJTGIPLParserState.java

javacc: maintainer-clean
	ln -s ../SimpleNode.java SimpleNode.java
	$(BASE_GIPSY_DIR)/tools/javacc/jjtree $(LIB_GIPSY_DIR) GIPL.jjt && \
    $(BASE_GIPSY_DIR)/tools/javacc/javacc $(LIB_GIPSY_DIR) GIPL.jj
	make maintainer-clean

clean:
	rm -f $(CLASSFILES) *.class

maintainer-clean: clean
	rm -f *.log *.diff $(JAVACCTMPFILES) GIPL.jj

# EOF
