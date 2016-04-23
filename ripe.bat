@rem
@rem $Id: ripe.bat,v 1.1 2011/01/08 00:20:18 mokhov Exp $
@rem


@echo off

java -cp bin;src;lib\marf.jar;lib\netcdfAll.jar;lib\junit.jar;lib\jini-core.jar;lib\jini-ext.jar;lib\jms.jar;lib\imq.jar;. gipsy.RIPE.RIPE %1 %2 %3 %4 %5 %6 %7 %8 %9

@rem TO CONVERT:

@rem CLASSPATH="$CLASSPATH:.:src:lib/marf.jar:marf.jar:lib/netcdfAll.jar:netcdfAll.jar"

@rem echo "Trying .class ..." >ripe.err.log
@rem java -cp $CLASSPATH gipsy.RIPE.RIPE $@ 2>>ripe.err.log

@rem if [ $? != 0 ]; then

@rem 	echo "Trying /usr/bin/gipsy/ripe.jar ..." >>ripe.err.log
@rem 	java -cp $CLASSPATH -jar /usr/bin/gipsy/ripe.jar $@ 2>>ripe.err.log

@rem 	if [ $? != 0 ]; then

@rem 		echo "Trying ripe.jar ..." >>ripe.err.log
@rem 		java -cp $CLASSPATH -jar ripe.jar $@ 2>>ripe.err.log

@rem 		if [ $? != 0 ]; then

@rem 			echo "Trying src/gipsy/RIPE/ripe.jar ..." >>ripe.err.log
@rem 			java -cp $CLASSPATH -jar src/gipsy/RIPE/ripe.jar $@ 2>>ripe.err.log

@rem 			if [ $? != 0 ]; then
@rem 				echo "ripe.jar or RIPE.class not found"
@rem 				exit $?
@rem 			fi

@rem 		fi

@rem 	fi

@rem fi


@rem EOF
