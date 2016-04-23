#!/bin/bash

#
# $Id: startGMTNode.sh,v 1.1 2010/12/25 05:42:03 mokhov Exp $
#

GIPSYROOT=~/workspace/gipsy
GIPSYLIB=$GIPSYROOT/lib
GIPSYCLASSPATH=$GIPSYROOT/bin

java -cp $GIPSYCLASSPATH:$GIPSYLIB/marf.jar:$GIPSYLIB/start.jar:$GIPSYLIB/tools.jar:$GIPSYLIB/sun-util.jar:$GIPSYLIB/jini-core.jar:$GIPSYLIB/jini-ext.jar:$GIPSYLIB/reggie-dl.jar:$GIPSYLIB/outrigger-dl.jar;$GIPSYLIB/mahalo-dl.jar gipsy.tests.junit.GEE.multitier.GIPSYNodeTestDriver GMTNode.config

# EOF
