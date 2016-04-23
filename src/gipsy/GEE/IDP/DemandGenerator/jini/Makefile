# GEE IDP DemandGenerator Jini Makefile
#
# $Id: Makefile,v 1.1 2011/03/12 04:56:42 mokhov Exp $
#

BASE_GIPSY_DIR=../../../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = 

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)
	$(MAKE) -C rmi $@
	$(MAKE) -C jeri $@

clean:
	rm -f $(CLASSFILES)
	$(MAKE) -C rmi $@
	$(MAKE) -C jeri $@

maintainer-clean: clean
	rm -f *.log *.diff *~
	$(MAKE) -C rmi $@
	$(MAKE) -C jeri $@

# EOF
