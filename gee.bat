@rem
@rem $Id: gee.bat,v 1.1 2011/01/06 18:43:40 mokhov Exp $
@rem


@echo off

java -cp bin;src;lib\marf.jar;lib\netcdfAll.jar;lib\junit.jar;lib\jini-core.jar;lib\jini-ext.jar;lib\jms.jar;lib\imq.jar;. gipsy.GEE.GEE %1 %2 %3 %4 %5 %6 %7 %8 %9


@rem EOF
