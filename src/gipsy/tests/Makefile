# Testing Modules Makefile
# Serguei Mokhov
#
# $Id: Makefile,v 1.10 2011/02/05 07:59:31 mokhov Exp $
#

VERSION=1.0.0-devel-`date +%Y%m%d`

BASE_GIPSY_DIR=../..
include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	Regression.class

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

all: $(CLASSFILES)
	$(MAKE) -C GEE $@
	$(MAKE) -C GIPC $@
	$(MAKE) -C junit $@
	( \
		rm -f *.jar && \
		cd ../.. && \
		ln -f -s ../COPYRIGHT COPYRIGHT && \
		jar cvmf gipsy/tests/manifest.mf gipsy/tests/Regression-$(VERSION).jar \
			COPYRIGHT \
			gipsy/GIPC \
			gipsy/GEE \
			gipsy/tests \
			gipsy/interfaces \
			gipsy/lang \
			gipsy/storage \
			gipsy/util \
			gipsy/wrappers \
	)
	ln -s -f Regression-$(VERSION).jar Regression.jar
	@echo "(-: Test Suite build has been successful :-)"

Regression.class: Regression.java

clean:
	rm -f *.class
	$(MAKE) -C GEE $@
	$(MAKE) -C GIPC $@
	$(MAKE) -C junit $@

maintainer-clean: clean
	rm -f *.log *.diff *.jar *.gipsy
	$(MAKE) -C GEE $@
	$(MAKE) -C GIPC $@
	$(MAKE) -C junit $@

# EOF
