#!/bin/bash

#
# Starts PS MARFCAT Node
#
# Serguei Mokhov
#
# $Id: startMARFCATGMTNode.sh,v 1.2 2012/06/26 03:19:15 s_rabah Exp $
#

GIPSYROOT=`pwd`/..
GIPSYLIB=$GIPSYROOT/../lib
GIPSYCLASSPATH=$GIPSYROOT:$GIPSYLIB/marf.jar:$GIPSYLIB/marfcat.jar:$GIPSYLIB/start.jar:$GIPSYLIB/tools.jar:$GIPSYLIB/sun-util.jar:$GIPSYLIB/jini-core.jar:$GIPSYLIB/jini-ext.jar:$GIPSYLIB/jms.jar:$GIPSYLIB/imq.jar:$GIPSYLIB/commons/commons-lang3-3.4.jar:$GIPSYROOT/../src/gipsy/gipsy.jar:$GIPSYLIB/joda-time/joda-time-2.4.jar

function usage() {
    echo "usage: ./startMARFCATGMT.sh [OTPS]"
    echo "          -n [gmtd|jini]  :: pipe command to STDIN : "
    echo "                          ::                  gmtd => 'start GMT GMTJini.config gmtd' "
    echo "                          ::                  jini => 'start GMT GMTJini.config'      "
    echo "          -u              :: display usage"
}

while getopts "n:u" opt; do
    case "$opt" in
        n)
            OPT_NODE=$OPTARG
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
        --node=marfcatGMTNode.config"

if [[ $OPT_NODE == "gmtd" ]]; then
    echo "start GMT GMTJini.config gmtd" | $JAVA_CMD > >(tee out.log) 2> >(tee err.log >&2)
elif [[ $OPT_NODE == "jini" ]]; then
    echo "start GMT GMTJini.config"      | $JAVA_CMD > >(tee out.log) 2> >(tee err.log >&2)
else
    $JAVA_CMD
fi

# EOF
