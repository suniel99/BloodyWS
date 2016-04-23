@rem *********************************************************
@rem **** Batch file
@rem *********************************************************
@rem **** by Yi Ji
@rem *********************************************************


@rem go to the bin folder
cd ../../../../..

set GIPSYLIB=../lib
set GIPSYCLASSES=./

java -cp %GIPSYLIB%\jini-core.jar;%GIPSYLIB%\jini-ext.jar;%GIPSYLIB%\reggie-dl.jar;%GIPSYLIB%\outrigger-dl.jar;%GIPSYLIB%\start.jar;%GIPSYLIB%\sun-util.jar;%GIPSYLIB%\marf.jar;%GIPSYCLASSES% ^
           gipsy.tests.GEE.IDP.DemandStore.ConnectionCapacityTest
           
pause