# GenericTranslator Makefile
# Serguei Mokhov
#
# $Header: /cvsroot/gipsy/gipsy/src/gipsy/GIPC/intensional/GenericTranslator/Makefile,v 1.7 2005/09/11 14:06:54 mokhov Exp $
#

BASE_GIPSY_DIR=../../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/marf.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	G.class \
	T.class \
	TranslationItem.class \
	TranslationLexer.class \
	TranslationParser.class \
	Translator.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

G.class: G.java
T.class: T.java
TranslationItem.class: TranslationItem.java
TranslationLexer.class: TranslationLexer.java
TranslationParser.class: TranslationParser.java
Translator.class: Translator.java

clean:
	rm -f $(CLASSFILES) *.class

maintainer-clean: clean
	rm -f *~ *.local *.log

# EOF
