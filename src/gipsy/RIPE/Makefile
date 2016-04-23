# General RIPE Makefile
# Serguei Mokhov
#
# $Id: Makefile,v 1.9 2012/06/19 17:44:13 mokhov Exp $
#

VERSION=1.0.0-devel-`date +%Y%m%d`

BASE_GIPSY_DIR = ../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:$(LIB_GIPSY_DIR)/jung-visualization-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-algorithms-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-graph-impl-2.0.1.jar::$(LIB_GIPSY_DIR)/jgoodies/forms-1.3.0.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	RIPE.class \
	RIPEException.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

#
# Java Doc for API doc generation
#

JAVADOC = javadoc
JAVADOCDIR = ../..
JAVADOCFLAGS = -verbose -use -version -author \
               -stylesheetfile ../doc/src/sgml/stylesheet.css

all: $(CLASSFILES) copyright
	$(MAKE) -C RuntimeSystem $@
	$(MAKE) -C debugger $@
	$(MAKE) -C editors $@
	( \
		rm -f *.jar && \
		cd ../.. && \
		ln -f -s ../COPYRIGHT COPYRIGHT && \
		jar cvmf gipsy/RIPE/manifest.mf gipsy/RIPE/ripe-$(VERSION).jar \
			COPYRIGHT \
			gipsy/GIPC \
			gipsy/GEE \
			gipsy/RIPE \
			gipsy/interfaces \
			gipsy/lang \
			gipsy/storage \
			gipsy/util \
			gipsy/wrappers \
	)
	ln -s -f ripe-$(VERSION).jar ripe.jar
	@echo "(-: RIPE build has been successful :-)"

copyright: ../../../COPYRIGHT
	ln -f -s ../../../COPYRIGHT COPYRIGHT

RIPE.class: RIPE.java
RIPEException.class: RIPEException.java

clean:
	rm -f $(CLASSFILES)
	$(MAKE) -C RuntimeSystem $@
	$(MAKE) -C debugger $@
	$(MAKE) -C editors $@

maintainer-clean: clean
	rm -f *.log *.diff *.jar COPYRIGHT
	$(MAKE) -C RuntimeSystem $@
	$(MAKE) -C debugger $@
	$(MAKE) -C editors $@

# EOF
