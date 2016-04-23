#!/bin/bash

cat JGIPL.jjt | \
	# Filter out unneeded stuff
	grep -v '// EOF' | \
	# Fix package
	sed 's/intensional\.SIPL\.JLucid/intensional\.SIPL\.ObjectiveLucid/g' | \
	# ObjectiveLucid GIPL
	sed 's/JGIPL/ObjectiveGIPL/' | \
	sed 's/\/\/{EXTEND-E1}/\/\/{EXTEND-E1}\n\t\t\t| ( <DOT> ID() ) #OBJREF E1()/' \
	> ObjectiveGIPL.jjt

# EOF
