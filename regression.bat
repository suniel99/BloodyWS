@rem
@rem $Id: regression.bat,v 1.1 2011/01/08 00:20:18 mokhov Exp $
@rem


@echo off

java -cp bin;src;lib\marf.jar;lib\netcdfAll.jar;lib\junit.jar;lib\jini-core.jar;lib\jini-ext.jar;lib\jms.jar;lib\imq.jar;. gipsy.tests.Regression %1 %2 %3 %4 %5 %6 %7 %8 %9

@rem TO CONVERT:
@rem  rm -rf src/tests/current
@rem  mkdir -p src/tests/current
@rem  mv src/tests/gipl/{*.log *.nc *.gipsy} src/tests/current/gipl
@rem  ...
@rem  diff -rc src/tests/expected src/tests/current

@rem if [ $? != 0 ]; then
@rem 	echo "There are differences between the current and expected outputs."
@rem 	echo "This may indicate an error in the current code or unupdated expected output."
@rem fi


@rem EOF
