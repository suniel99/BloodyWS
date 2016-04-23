# Builds the GIPSY project and its Modules
# Serguei Mokhov
#
# $Id: Makefile,v 1.27 2013/02/23 01:51:16 mokhov Exp $
#
# Use with GNU make.
#

VERSION=1.0.0-devel-`date +%Y%m%d`

BASE_GIPSY_DIR = ..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:$(LIB_GIPSY_DIR)/jung-visualization-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-algorithms-2.0.1.jar:$(LIB_GIPSY_DIR)/jung-graph-impl-2.0.1.jar:$(LIB_GIPSY_DIR)/jgoodies/forms-1.3.0.jar:$(LIB_GIPSY_DIR)/marfcat.jar:$(LIB_GIPSY_DIR)/commons/commons-lang3-3.4.jar:$(LIB_GIPSY_DIR)/joda-time/joda-time-2.4.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

#
# Java Doc for API doc generation
#

JAVADOC = javadoc
JAVADOCDIR = ..
JAVADOCFLAGS = -verbose -use -version -author -classpath $(CLASSPATH) -sourcepath .. -subpackages gipsy

CLASSFILES = \
	GIPSY.class \
	Configuration.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)
	$(MAKE) -C apps $@
	$(MAKE) -C GEE $@
	$(MAKE) -C GIPC $@
	$(MAKE) -C RIPE $@
	$(MAKE) -C util $@
	$(MAKE) -C interfaces $@
	$(MAKE) -C storage $@
	$(MAKE) -C tests $@
	$(MAKE) -C wrappers $@
	$(MAKE) -C lang $@
	(\
		cd .. && \
		ln -f -s ../COPYRIGHT COPYRIGHT && \
		rm -f gipsy/gipsy.jar && \
		jar cvmf gipsy/manifest.mf gipsy/gipsy-$(VERSION).jar \
			COPYRIGHT \
			gipsy \
	)
	ln -s -f gipsy-$(VERSION).jar gipsy.jar
	@echo "(-: GIPSY build has been successful :-)"

GIPSY.class: GIPSY.java
Configuration.class: Configuration.java

run: all
	java -classpath $(CLASSPATH) -sourcepath .. gipsy.GIPSY --debug

#
# API docs in javadoc
#

# API Documentation for GIPSY users
apidoc: $(JAVAFILES)
	mkdir -p $(JAVADOCDIR)/api
	$(JAVADOC) $(JAVADOCFLAGS) -package -d $(JAVADOCDIR)/api $(JAVAFILES)

# API Documentation for GIPSY developers
apidoc-dev: $(JAVAFILES)
	mkdir -p $(JAVADOCDIR)/api-dev
	$(JAVADOC) $(JAVADOCFLAGS) -private -d $(JAVADOCDIR)/api-dev $(JAVAFILES)

# Both APIs
api: apidoc apidoc-dev

clean:
	rm -rf classes *.class
	$(MAKE) -C apps $@
	$(MAKE) -C GIPC $@
	$(MAKE) -C GEE $@
	$(MAKE) -C RIPE $@
	$(MAKE) -C util $@
	$(MAKE) -C interfaces $@
	$(MAKE) -C storage $@
	$(MAKE) -C tests $@
	$(MAKE) -C wrappers $@
	$(MAKE) -C lang $@

maintainer-clean: clean
	rm -f *.jar *.log *.diff *~
	$(MAKE) -C apps $@
	$(MAKE) -C GIPC $@
	$(MAKE) -C GEE $@
	$(MAKE) -C RIPE $@
	$(MAKE) -C util $@
	$(MAKE) -C interfaces $@
	$(MAKE) -C storage $@
	$(MAKE) -C tests $@
	$(MAKE) -C wrappers $@
	$(MAKE) -C lang $@
	$(MAKE) -C apps$@

# EOF
