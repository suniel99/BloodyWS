# SIPL Makefile
# Serguei Mokhov
#
# $Header: /cvsroot/gipsy/gipsy/src/gipsy/GIPC/intensional/SIPL/Makefile,v 1.7 2005/06/24 23:04:57 mokhov Exp $
#

BASE_GIPSY_DIR = ../../../..

include $(BASE_GIPSY_DIR)/../Makefile.global

all:
	$(MAKE) -C IndexicalLucid $@
	$(MAKE) -C JLucid $@
	$(MAKE) -C ObjectiveLucid $@
	$(MAKE) -C Onyx $@
	$(MAKE) -C Lucx $@
	$(MAKE) -C GLU $@
	$(MAKE) -C TensorLucid $@

clean:
	$(MAKE) -C IndexicalLucid $@
	$(MAKE) -C JLucid $@
	$(MAKE) -C ObjectiveLucid $@
	$(MAKE) -C Onyx $@
	$(MAKE) -C Lucx $@
	$(MAKE) -C GLU $@
	$(MAKE) -C TensorLucid $@

maintainer-clean:
	rm -f *.log *.diff
	$(MAKE) -C IndexicalLucid $@
	$(MAKE) -C JLucid $@
	$(MAKE) -C ObjectiveLucid $@
	$(MAKE) -C Onyx $@
	$(MAKE) -C Lucx $@
	$(MAKE) -C GLU $@
	$(MAKE) -C TensorLucid $@

# EOF
