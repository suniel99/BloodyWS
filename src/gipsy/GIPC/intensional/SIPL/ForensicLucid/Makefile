# Forensic Lucid Makefile
#
# $Id: Makefile,v 1.3 2013/07/18 13:45:43 mokhov Exp $
#

BASE_GIPSY_DIR = ../../../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:$(LIB_GIPSY_DIR)/jung-visualization-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-algorithms-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-graph-impl-2.0.1.jar::$(LIB_GIPSY_DIR)/jgoodies/forms-1.3.0.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	ForensicLucidCompiler.class \
	ForensicLucidSemanticAnalyzer.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

ForensicLucidCompiler.class: ForensicLucidCompiler.java
ForensicLucidSemanticAnalyzer.class: ForensicLucidSemanticAnalyzer.java

JAVACCTMPFILES = \
	Node.java \
	ParseException.java \
	SimpleCharStream.java \
	SimpleNode.java \
	Token.java \
	TokenMgrError.java

javacc: maintainer-clean
	ln -sf ../../SimpleNode.java SimpleNode.java
	$(BASE_GIPSY_DIR)/tools/javacc/jjtree $(LIB_GIPSY_DIR) ForensicLucid.jjt && \
	$(BASE_GIPSY_DIR)/tools/javacc/javacc $(LIB_GIPSY_DIR) ForensicLucid.jj
	make maintainer-clean

clean:
	rm -f $(CLASSFILES) *.class

maintainer-clean: clean
	rm -f *.log *.diff *.jj $(JAVACCTMPFILES)

# EOF
