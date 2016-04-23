#
# Main GIPSY Makefile to build under UNIXen
# Serguei Mokhov
#
# $Header: /cvsroot/gipsy/gipsy/Makefile,v 1.11 2011/02/05 23:17:21 mokhov Exp $
#

VERSION=1.0.0-devel-`date +%Y%m%d`

BASE_GIPSY_DIR=src

include Makefile.global

all clean:
	$(MAKE) -C src $@
	$(MAKE) -C doc $@

#
# Distro
#

distro: distro.exclude
	rm -rf distro
	mkdir -p distro/doc
	@echo "Making GIPSY source distro..."
	make -C src $@
	@echo "Making GIPSY docs distro..."
	make -C doc $@
	@echo "Cleaning up after builds..."
	make maintainer-clean
	@echo "Making GIPSY source bundle..."
	tar --exclude-from distro.exclude \
        -cvf gipsy-src-$(VERSION).tar \
        src doc README TODO gipsy.jpx Makefile COPYRIGHT \
        nbproject build.xml manifest.mf
	gzip --best gipsy-src-$(VERSION).tar
	mv gipsy-src-$(VERSION).tar.gz distro/
	@echo "Bundling up on-line documentation..."
	cp -r COPYRIGHT ../api ../api-dev distro/doc
	( \
		cd distro/; \
		tar -cvf gipsy-doc-$(VERSION).tar doc; \
		gzip --best "gipsy-doc-$(VERSION).tar"; \
		rm -rf doc; \
	)
	@echo "Done building source and documentation distros for version $(VERSION)."


maintainer-clean:
	$(MAKE) -C src $@
	$(MAKE) -C doc $@
	rm -rf *.diff *.log *~ bin/

clean-gipsy-jar:
	rm -rf src/gipsy/GEE/gee*.jar
	rm -rf src/gipsy/GIPC/gipc*.jar
	rm -rf src/gipsy/RIPE/ripe*.jar
	rm -rf src/gipc/tests/Regression*.jar
	rm -rf src/gipsy/gipsy*.jar

cdc: checkout-dynamic-config
checkout-dynamic-config:
	@git checkout bin/jini/mahalo.config bin/jini/outrigger.config bin/multitier/RegDSTTA.config

# EOF
