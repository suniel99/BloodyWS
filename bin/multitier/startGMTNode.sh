#!/bin/bash

#
# $Id: startGMTNode.sh,v 1.7 2012/06/26 03:19:16 s_rabah Exp $
#

GIPSYROOT=`pwd`/..
GIPSYLIB=$GIPSYROOT/../lib


GIPSYCLASSPATH=$GIPSYROOT:$GIPSYLIB/marf.jar:$GIPSYLIB/start.jar:$GIPSYLIB/tools.jar:$GIPSYLIB/sun-util.jar:$GIPSYLIB/jini-core.jar:$GIPSYLIB/jini-ext.jar:$GIPSYLIB/jms.jar:$GIPSYLIB/imq.jar:$GIPSYLIB/outrigger.jar:$GIPSYROOT/../src/gipsy/GEE/gee.jar


echo "Diagnostics:"
echo "GIPSYROOT=$GIPSYROOT"
pwd
echo "GIPSYCLASSPATH=$GIPSYCLASSPATH"
java -version

java \
	-cp $GIPSYCLASSPATH \
	-Xms768m -Xmx768m -Xss320k \
	-Djava.security.policy=$GIPSYROOT/jini/start.policy \
	gipsy.GEE.GEE \
	--node=GMTNode.config

# EOF
