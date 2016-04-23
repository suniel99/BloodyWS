#!/bin/bash

cat ../../GIPL/GIPL.jjt | \
	# Filter out unneeded stuff
	grep -v '// EOF' | \
	#grep -v 'import gipsy.GIPC.intensional.SimpleNode' | \
	# Fix package
	sed 's/intensional\.GIPL/intensional\.SIPL\.JLucid/g' | \
	# JLucid GIPL
	sed 's/GIPL/JGIPL/' | \
	sed 's/\/\/{EXTEND-E}/\/\/{EXTEND-E}\n\t\t| embed()/' | \
	sed 's/\/\/{EXTEND-FACTOR}/\/\/{EXTEND-FACTOR}\n\t| array()/' | \
	sed 's/<WHERE: "where">/<WHERE: "where">\n\t| <EMBED: "embed">/g' \
	> JGIPL.jjt

./jlucid.sh >> JGIPL.jjt

# EOF
