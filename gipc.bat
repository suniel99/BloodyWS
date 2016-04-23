@rem
@rem $Id: gipc.bat,v 1.1 2011/01/08 00:20:18 mokhov Exp $
@rem


@echo off

java -cp bin;src;lib\marf.jar;lib\netcdfAll.jar;lib\junit.jar;lib\jini-core.jar;lib\jini-ext.jar;lib\jms.jar;lib\imq.jar;. gipsy.GIPC.GIPC %1 %2 %3 %4 %5 %6 %7 %8 %9

@rem TO CONVERT:


@rem CLASSPATH="$CLASSPATH:.:src:lib/marf.jar:marf.jar:lib/netcdfAll.jar:netcdfAll.jar"
@rem JAVA=java

@rem echo "Trying .class ..." >gipc.err.log
@rem $JAVA -cp $CLASSPATH gipsy.GIPC.GIPC $@ 2>>gipc.err.log

@rem if [ $? != 0 ]; then
@rem 	echo "Trying /usr/bin/gipsy/gipc.jar ..." >>gipc.err.log
@rem 	$JAVA -cp $CLASSPATH -jar /usr/bin/gipsy/gipc.jar $@ 2>>gipc.err.log
@rem 	if [ $? != 0 ]; then
@rem 		echo "Trying gipc.jar ..." >>gipc.err.log
@rem 		$JAVA -cp $CLASSPATH -jar gipc.jar $@ 2>>gipc.err.log
@rem 		if [ $? != 0 ]; then
@rem 			echo "Trying src/gipsy/GIPC/gipc.jar ..." >>gipc.err.log
@rem 			$JAVA -cp $CLASSPATH -jar src/gipsy/GIPC/gipc.jar $@ 2>>gipc.err.log
@rem 			if [ $? != 0 ]; then
@rem 				echo "gipc.jar or GIPC.class not found"
@rem 				exit $?
@rem 			fi
@rem 		fi
@rem 	fi
@rem fi


@rem EOF
