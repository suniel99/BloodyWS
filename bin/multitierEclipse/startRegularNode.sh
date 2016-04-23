#!/bin/bash

#
# $Id: startRegularNode.sh,v 1.2 2011/04/29 20:58:17 ji_yi Exp $
#

GIPSYROOT=../../
GIPSYLIB=%GIPSYROOT%/lib
GIPSYCLASSPATH=$GIPSYROOT/bin

java -cp $GIPSYCLASSPATH:$GIPSYLIB/marf.jar:$GIPSYLIB/tools.jar:$GIPSYLIB/sun-util.jar:$GIPSYLIB/jini-core.jar:$GIPSYLIB/jini-ext.jar:$GIPSYLIB/start.jar:$GIPSYLIB/reggie-dl.jar:$GIPSYLIB/outrigger-dl.jar gipsy.tests.junit.GEE.multitier.GIPSYNodeTestDriver RegularNode.config

# EOF
