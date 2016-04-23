#!/bin/bash

#
# $Id: startMARFCATRegularNode.sh,v 1.2 2012/06/26 03:19:16 s_rabah Exp $
#

GIPSYROOT=`pwd`/..
GIPSYLIB=$GIPSYROOT/../lib

GIPSYCLASSPATH=$GIPSYROOT:$GIPSYLIB/marf.jar:$GIPSYLIB/marfcat.jar:$GIPSYLIB/start.jar:$GIPSYLIB/tools.jar:$GIPSYLIB/sun-util.jar:$GIPSYLIB/jini-core.jar:$GIPSYLIB/jini-ext.jar:$GIPSYLIB/jms.jar:$GIPSYLIB/imq.jar:$GIPSYLIB/commons/commons-lang3-3.4.jar:$GIPSYROOT/../src/gipsy/gipsy.jar:$GIPSYLIB/joda-time/joda-time-2.4.jar

function usage() {
    echo "usage: ./startMARFCATGMT.sh [OTPS]"
    echo "          -r              :: register right away"
    echo "          -u              :: display usage"
}


OPT_REGISTER=0
OPT_TAG=1
LOG_FILENAME=regular

while getopts "ru" opt; do
    case "$opt" in
        r)
            OPT_REGISTER=1
        ;;
        *|u)  
            usage
            exit 0
        ;;
    esac
done
shift $((OPTIND -1)) 

echo "Diagnostics:"
echo "GIPSYROOT=$GIPSYROOT"
pwd
echo "GIPSYCLASSPATH=$GIPSYCLASSPATH"
java -version

JAVA_CMD="java \
	-cp $GIPSYCLASSPATH \
	-Xms768m -Xmx768m -Xss320k \
    -Djava.rmi.server.useCodebaseOnly=false \
	-Djava.security.policy=start.policy \
	gipsy.GEE.GEE \
	--node=marfcatRegularNode.config"

if [[ $OPT_REGISTER == 1 ]]; then
    echo "register" | $JAVA_CMD > >(tee "$LOG_FILENAME".out) 2> >(tee "$LOG_FILENAME".err >&2)
else
    $JAVA_CMD
fi

# EOF
