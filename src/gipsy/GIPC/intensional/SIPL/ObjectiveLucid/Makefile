# ObjectiveLucid Makefile
# Serguei Mokhov
#
# $Header: /cvsroot/gipsy/gipsy/src/gipsy/GIPC/intensional/SIPL/ObjectiveLucid/Makefile,v 1.3 2005/09/05 02:54:38 mokhov Exp $
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

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	JJTObjectiveGIPLParserState.class \
	JJTObjectiveIndexicalLucidParserState.class \
	ObjectiveGIPLParser.class \
	ObjectiveGIPLParserConstants.class \
	ObjectiveGIPLParserTokenManager.class \
	ObjectiveGIPLParserTreeConstants.class \
	ObjectiveIndexicalLucidParser.class \
	ObjectiveIndexicalLucidParserConstants.class \
	ObjectiveIndexicalLucidParserTokenManager.class \
	ObjectiveIndexicalLucidParserTreeConstants.class \
	ObjectiveLucidCompiler.class \
	ObjectiveLucidParser.class \
	ObjectiveLucidPreprocessor.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

javacc: maintainer-clean
	ln -s ../../SimpleNode.java SimpleNode.java
	ln -s -f ../JLucid/jlucid.sh jlucid.sh
	ln -s -f ../JLucid/JGIPL.sh JGIPL.sh
	./JGIPL.sh
	./ObjectiveGIPL.sh
	$(BASE_GIPSY_DIR)/tools/javacc/jjtree $(LIB_GIPSY_DIR) ObjectiveGIPL.jjt \
		&& $(BASE_GIPSY_DIR)/tools/javacc/javacc $(LIB_GIPSY_DIR) ObjectiveGIPL.jj
	ln -s ../JLucid/JIndexicalLucid.sh JIndexicalLucid.sh
	./JIndexicalLucid.sh
	./ObjectiveIndexicalLucid.sh
	$(BASE_GIPSY_DIR)/tools/javacc/jjtree $(LIB_GIPSY_DIR) ObjectiveIndexicalLucid.jjt \
		&& $(BASE_GIPSY_DIR)/tools/javacc/javacc $(LIB_GIPSY_DIR) ObjectiveIndexicalLucid.jj
	make maintainer-clean

JJTObjectiveGIPLParserState.class: JJTObjectiveGIPLParserState.java
JJTObjectiveIndexicalLucidParserState.class: JJTObjectiveIndexicalLucidParserState.java
ObjectiveGIPLParser.class: ObjectiveGIPLParser.java
ObjectiveGIPLParserConstants.class: ObjectiveGIPLParserConstants.java
ObjectiveGIPLParserTokenManager.class: ObjectiveGIPLParserTokenManager.java
ObjectiveGIPLParserTreeConstants.class: ObjectiveGIPLParserTreeConstants.java
ObjectiveIndexicalLucidParser.class: ObjectiveIndexicalLucidParser.java
ObjectiveIndexicalLucidParserConstants.class: ObjectiveIndexicalLucidParserConstants.java
ObjectiveIndexicalLucidParserTokenManager.class: ObjectiveIndexicalLucidParserTokenManager.java
ObjectiveIndexicalLucidParserTreeConstants.class: ObjectiveIndexicalLucidParserTreeConstants.java
ObjectiveLucidCompiler.class: ObjectiveLucidCompiler.java
ObjectiveLucidParser.class: ObjectiveLucidParser.java
ObjectiveLucidPreprocessor.class: ObjectiveLucidPreprocessor.java

clean:
	rm -f $(CLASSFILES) *.class

maintainer-clean: clean
	rm -f $(JAVACCTMPFILES) *.diff *.log *~ *.jj* J*.sh jlucid.sh

# EOF
