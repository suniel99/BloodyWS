#!/bin/bash

cat ../../SIPL/IndexicalLucid/IndexicalLucid.jjt | \
	# Filter out unneeded stuff
	grep -v '// EOF' | \
	#grep -v 'import gipsy.GIPC.intensional.SimpleNode' | \
	# Fix package
	sed 's/intensional\.SIPL\.IndexicalLucid/intensional\.SIPL\.JLucid/g' | \
	# JLucid Indexical
	sed 's/IndexicalLucid/JIndexicalLucid/' | \
	sed 's/\/\/{EXTEND-E}/\/\/{EXTEND-E}\n\t\t| embed()/' | \
	sed 's/\/\/{EXTEND-FACTOR}/\/\/{EXTEND-FACTOR}\n\t| array()/' | \
	sed 's/<WHERE: "where">/<WHERE: "where">\n\t| <EMBED: "embed">/g' \
	> JIndexicalLucid.jjt

./jlucid.sh >> JIndexicalLucid.jjt

# EOF
