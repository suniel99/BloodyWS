# General GEE Makefile
# Serguei Mokhov
#
# $Id: Makefile,v 1.15 2013/02/23 01:51:19 mokhov Exp $
#

VERSION=1.0.0-devel-`date +%Y%m%d`

BASE_GIPSY_DIR = ../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:$(LIB_GIPSY_DIR)/jung-visualization-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-algorithms-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-graph-impl-2.0.1.jar::$(LIB_GIPSY_DIR)/jgoodies/forms-1.3.0.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	CONFIG.class \
	Executor.class \
	GEE.class \
	GEEException.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1


all: $(CLASSFILES)
	$(MAKE) -C evaluation $@
	$(MAKE) -C IDP $@
	$(MAKE) -C IVW $@
	$(MAKE) -C multitier $@
	( \
		cd ../.. && \
		ln -f -s ../COPYRIGHT COPYRIGHT && \
		rm -f gipsy/GEE/gee.jar && \
		jar cvmf gipsy/GEE/manifest.mf gipsy/GEE/gee-$(VERSION).jar \
			COPYRIGHT \
			gipsy/GEE \
			gipsy/interfaces \
			gipsy/lang \
			gipsy/storage \
			gipsy/util \
			gipsy/wrappers \
	)
	ln -s -f gee-$(VERSION).jar gee.jar
	@echo "(-: GEE build has been successful :-)"


CONFIG.class: CONFIG.java
Executor.class: Executor.java
GEE.class: GEE.java
GEEException.class: GEEException.java

clean:
	$(MAKE) -C evaluation $@
	rm -f *.class
	$(MAKE) -C IDP $@
	$(MAKE) -C IVW $@
	$(MAKE) -C multitier $@

maintainer-clean: clean
	rm -f *.log *.diff *.jar *~
	$(MAKE) -C evaluation $@
	$(MAKE) -C IDP $@
	$(MAKE) -C IVW $@
	$(MAKE) -C multitier $@

# EOF
