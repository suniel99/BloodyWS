# JLucid Makefile
#
# $Header: /cvsroot/gipsy/gipsy/src/gipsy/GIPC/intensional/SIPL/JLucid/Makefile,v 1.6 2005/09/05 02:54:36 mokhov Exp $
#

BASE_GIPSY_DIR = ../../../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/marf.jar:.

JAVACCTMPFILES = \
    Node.java \
    ParseException.java \
    SimpleCharStream.java \
    Token.java \
    TokenMgrError.java \
	SimpleNode.java

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/marf.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	JGIPLParser.class \
	JGIPLParserConstants.class \
	JGIPLParserTokenManager.class \
	JGIPLParserTreeConstants.class \
	JIndexicalLucidParser.class \
	JIndexicalLucidParserConstants.class \
	JIndexicalLucidParserTokenManager.class \
	JIndexicalLucidParserTreeConstants.class \
	JJTJGIPLParserState.class \
	JJTJIndexicalLucidParserState.class \
	JLucidCompiler.class \
	JLucidParser.class \
	JLucidPreprocessor.class \
	JavaSource.class \
	LucidSource.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

javacc: maintainer-clean
	ln -s ../../SimpleNode.java SimpleNode.java
	./JGIPL.sh
	$(BASE_GIPSY_DIR)/tools/javacc/jjtree $(LIB_GIPSY_DIR) JGIPL.jjt \
		&& $(BASE_GIPSY_DIR)/tools/javacc/javacc $(LIB_GIPSY_DIR) JGIPL.jj
	./JIndexicalLucid.sh
	$(BASE_GIPSY_DIR)/tools/javacc/jjtree $(LIB_GIPSY_DIR) JIndexicalLucid.jjt \
		&& $(BASE_GIPSY_DIR)/tools/javacc/javacc $(LIB_GIPSY_DIR) JIndexicalLucid.jj
	make maintainer-clean

JGIPLParser.class: JGIPLParser.java
JGIPLParserConstants.class: JGIPLParserConstants.java
JGIPLParserTokenManager.class: JGIPLParserTokenManager.java
JGIPLParserTreeConstants.class: JGIPLParserTreeConstants.java
JIndexicalLucidParser.class: JIndexicalLucidParser.java
JIndexicalLucidParserConstants.class: JIndexicalLucidParserConstants.java
JIndexicalLucidParserTokenManager.class: JIndexicalLucidParserTokenManager.java
JIndexicalLucidParserTreeConstants.class: JIndexicalLucidParserTreeConstants.java
JJTJGIPLParserState.class: JJTJGIPLParserState.java
JJTJIndexicalLucidParserState.class: JJTJIndexicalLucidParserState.java
JLucidCompiler.class: JLucidCompiler.java
JLucidParser.class: JLucidParser.java
JLucidPreprocessor.class: JLucidPreprocessor.java
JavaSource.class: JavaSource.java
LucidSource.class: LucidSource.java

clean:
	rm -f $(CLASSFILES) *.class

maintainer-clean: clean
	rm -f $(JAVACCTMPFILES) *.diff *.log *~ *.jj*

# EOF
