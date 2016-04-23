@rem *********************************************************
@rem **** Batch file to run the GIPSY DGT Simulator
@rem *********************************************************
@rem **** by Emil Vassev
@rem **** Last modification on June 05, 2007
@rem **** by Yi Ji
@rem **** Last modification on Sept. 02, 2009
@rem *********************************************************


@rem Runing GIPSY Demand Generator Dummy ....

cd ../../../..
set GIPSYLIB=D:\workspace\gipsy\lib
set GIPSYCLASSES=D:\workspace\gipsy\bin

java -cp %GIPSYLIB%\jini-core.jar;%GIPSYLIB%\jini-ext.jar;%GIPSYLIB%\marf.jar;%GIPSYLIB%\jms.jar;%GIPSYLIB%\fscontext.jar;%GIPSYLIB%\imq.jar;%GIPSYCLASSES% ^
           gipsy.tests.GEE.simulator.DGT