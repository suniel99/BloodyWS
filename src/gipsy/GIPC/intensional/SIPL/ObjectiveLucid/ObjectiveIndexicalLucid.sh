#!/bin/bash

cat JIndexicalLucid.jjt | \
	# Filter out unneeded stuff
	grep -v '// EOF' | \
	# Fix package
	sed 's/intensional\.SIPL\.JLucid/intensional\.SIPL\.ObjectiveLucid/g' | \
	# ObjectiveLucid Indexical
	sed 's/JIndexicalLucid/ObjectiveIndexicalLucid/' | \
	sed 's/\/\/{EXTEND-E1}/\/\/{EXTEND-E1}\n\t\t\t| ( <DOT> ID() ) #OBJREF E1()/' \
	> ObjectiveIndexicalLucid.jjt

# EOF
