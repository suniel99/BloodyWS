# General Warehouse Makefile
# Serguei Mokhov
#
# $Id: Makefile,v 1.4 2011/03/12 05:07:59 mokhov Exp $
#

BASE_GIPSY_DIR=../../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

LIB_GIPSY_DIR = $(BASE_GIPSY_DIR)/../lib
CLASSPATH = $(LIB_GIPSY_DIR)/junit.jar:$(LIB_GIPSY_DIR)/marf.jar:$(LIB_GIPSY_DIR)/netcdfAll.jar:$(LIB_GIPSY_DIR)/jini-core.jar:$(LIB_GIPSY_DIR)/jini-ext.jar:$(LIB_GIPSY_DIR)/jms.jar:$(LIB_GIPSY_DIR)/imq.jar:.

JFLAGS = $(COMMONJFLAGS) -classpath $(CLASSPATH) -sourcepath $(BASE_GIPSY_DIR)
JAVAC = javac

CLASSFILES = \
	Cache.class \
	CacheElement.class \
	CacheSizes.class \
	IVWControl.class \
	IVWInterface.class \
	IValueStore.class \
	NetCDFDataManager.class \
	NetCDFFileManager.class \
	ValueHouse.class

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $< >> compile.log 2>&1

all: $(CLASSFILES)

Cache.class: Cache.java
CacheElement.class: CacheElement.java
CacheSizes.class: CacheSizes.java
IVWControl.class: IVWControl.java
IVWInterface.class: IVWInterface.java
IValueStore.class: IValueStore.java
NetCDFDataManager.class: NetCDFDataManager.java
NetCDFFileManager.class: NetCDFFileManager.java
ValueHouse.class: ValueHouse.java

clean:
	rm -f $(CLASSFILES)

maintainer-clean: clean
	rm -f *.diff *.log *~

# EOF
