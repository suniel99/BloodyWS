#!/bin/bash
#
# $Id: start.sh,v 1.2 2012/04/09 01:28:18 mokhov Exp $
#

JAVA="java -Xss228k"
LIB="lib"

exec $JAVA \
     -Djava.security.policy=start.policy \
     -Djava.rmi.server.useCodebaseOnly=false \
     -jar $LIB/start.jar \
     startTransient4.config

# EOF
