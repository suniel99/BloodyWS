# SRC Makefile to build under UNIXen
# Serguei Mokhov
#
# $Header: /cvsroot/gipsy/gipsy/src/Makefile,v 1.6 2007/11/22 20:50:59 mokhov Exp $
#

BASE_GIPSY_DIR=.

include $(BASE_GIPSY_DIR)/../Makefile.global

all clean maintainer-clean:
	$(MAKE) -C gipsy $@
	$(MAKE) -C tools $@

run:
	@echo "Run specific target. Available targets are: translator"

translator: all
	java gipsy/GIPC/intensional/GenericTranslator/Translator

# EOF
