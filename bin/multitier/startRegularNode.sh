#!/bin/bash

#
# $Id: startRegularNode.sh,v 1.6 2012/06/26 03:19:15 s_rabah Exp $
#

GIPSYROOT=`pwd`/..
GIPSYLIB=$GIPSYROOT/../lib
GIPSYCLASSPATH=$GIPSYROOT:$GIPSYLIB/marf.jar:$GIPSYLIB/start.jar:/usr/java/latest/lib/tools.jar:$GIPSYLIB/sun-util.jar:$GIPSYLIB/jini-core.jar:$GIPSYLIB/jini-ext.jar:$GIPSYLIB/jms.jar:$GIPSYLIB/imq.jar:$GIPSYLIB/outrigger.jar::$GIPSYLIB/reggie-dl.jar:$GIPSYLIB/outrigger-dl.jar:$GIPSYLIB/mahalo-dl.jar:/usr/lib/jvm/java-6-openjdk/lib/tools.jar

echo "Diagnostics:"
echo "GIPSYROOT=$GIPSYROOT"
pwd
echo "GIPSYCLASSPATH=$GIPSYCLASSPATH"
java -version

java \
	-cp $GIPSYCLASSPATH \
	-Xms768m -Xmx768m -Xss320k \
	-Djava.security.policy=$GIPSYROOT/jini/start.policy \
	-Djava.library.path=/usr/lib/jvm/java-6-openjdk/jre/lib/i386/ \
	gipsy.GEE.GEE \
	--node=RegularNode.config

# EOF
