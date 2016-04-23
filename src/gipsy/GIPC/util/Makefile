# GIPC Utility Modules Makefile
# Serguei Mokhov
#
# $Header: /cvsroot/gipsy/gipsy/src/gipsy/GIPC/util/Makefile,v 1.3 2005/09/05 02:54:41 mokhov Exp $
#

BASE_GIPSY_DIR = ../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/netcdfAll.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	Node.class \
	ParseException.class \
	SimpleCharStream.class \
	Token.class \
	TokenMgrError.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

Node.class: Node.java
ParseException.class: ParseException.java
SimpleCharStream.class: SimpleCharStream.java
Token.class: Token.java
TokenMgrError.class: TokenMgrError.java

clean:
	rm -f $(CLASSFILES)

maintainer-clean: clean
	rm -f *.log *.diff

# EOF
