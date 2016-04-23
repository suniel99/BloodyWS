# GIPC/Preprocessing Makefile
#
# Serguei Mokhov, mokhov@cs.concordia.ca
#
# $Header: /cvsroot/gipsy/gipsy/src/gipsy/GIPC/Preprocessing/Makefile,v 1.9 2005/09/26 02:59:33 mokhov Exp $
#

BASE_GIPSY_DIR = ../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = .

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH):$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

JAVACCTMPFILES = \
    Node.java \
    ParseException.java \
    SimpleCharStream.java \
    Token.java \
    TokenMgrError.java


CLASSFILES = \
	Preprocessor.class \
	PreprocessorParserConstants.class \
	PreprocessorParserTreeConstants.class \
	SimpleNode.class \
	PreprocessorParser.class \
	JJTPreprocessorParserState.class \
	PreprocessorParserTokenManager.class \
	CodeSegment.class
	
.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $<

all: $(CLASSFILES)

javacc: maintainer-clean
	$(BASE_GIPSY_DIR)/tools/javacc/jjtree $(LIB_GIPSY_DIR) PreprocessorParser.jjt \
    && $(BASE_GIPSY_DIR)/tools/javacc/javacc $(LIB_GIPSY_DIR) PreprocessorParser.jj
	make maintainer-clean


Preprocessor.class: Preprocessor.java
PreprocessorParserConstants.class: PreprocessorParserConstants.java
PreprocessorParserTreeConstants.class: PreprocessorParserTreeConstants.java
SimpleNode.class: SimpleNode.java
PreprocessorParser.class: PreprocessorParser.java
JJTPreprocessorParserState.class: JJTPreprocessorParserState.java
PreprocessorParserTokenManager.class: PreprocessorParserTokenManager.java
CodeSegment.class: CodeSegment.java

clean:
	rm -f $(CLASSFILES) *.class

maintainer-clean: clean
	rm -f *.jj $(JAVACCTMPFILES)

# EOF
