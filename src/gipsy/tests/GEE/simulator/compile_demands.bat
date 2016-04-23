@rem *****************************************************************
@rem **** Batch file to compile the GIPSY-GTA Simulator demand classes
@rem *****************************************************************
@rem **** by Emil Vassev
@rem **** Last modification on June 05, 2007
@rem *****************************************************************


set JINILIB=E:\jini2_0_1\lib
set CLIENTDIR=E:\jini\client
set SERVICEDIR=E:\jini\service


rem Compiling the WorkResult* and WorkDemand* classes ....

javac -classpath %JINILIB%\jini-core.jar;%JINILIB%\jini-ext.jar;%JINILIB%\sun-util.jar;%CLIENTDIR% ^
      -d %CLIENTDIR% gipsy\demands\Pi.java gipsy\demands\WorkResultPi.java gipsy\demands\WorkDemandPi.java ^
      gipsy\demands\SerializedImage.java gipsy\demands\PrintScreen.java gipsy\demands\WorkResultScrSht.java ^
      gipsy\demands\WorkDemandScrSht.java gipsy\demands\WorkResultHD.java gipsy\demands\WorkDemandHD.java     


rem Copying the WorkResult* and WorkDemand* class files to the service directory ....

copy %CLIENTDIR%\gipsy\demands\*.* %SERVICEDIR%\gipsy\demands

