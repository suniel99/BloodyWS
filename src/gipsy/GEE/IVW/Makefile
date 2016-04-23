# General IVW Makefile
# Serguei Mokhov
#
# $Id: Makefile,v 1.8 2011/03/12 05:07:58 mokhov Exp $
#

BASE_GIPSY_DIR=../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

all clean maintainer-clean:
	rm -f *~
	$(MAKE) -C Warehouse $@
	$(MAKE) -C GarbageCollector $@

# EOF
