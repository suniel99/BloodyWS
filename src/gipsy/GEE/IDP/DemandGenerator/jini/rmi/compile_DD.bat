@rem *********************************************************
@rem **** Batch file to compile the GIPSY Demand Dispatcher
@rem *********************************************************
@rem **** by Emil Vassev
@rem **** Last modification on February 21, 2005
@rem *********************************************************


set JINILIB=E:\jini2_0_1\lib
set CLIENTDIR=E:\jini\client
set SERVICEDIR=E:\jini\service


rem Compiling GIPSY Demand Dispatcher ....

javac -classpath %JINILIB%\jini-core.jar;%JINILIB%\jini-ext.jar;%JINILIB%\sun-util.jar;%CLIENTDIR% ^
      -d %CLIENTDIR%^
      DMSException.java LUSException.java DemandDispatcherException.java ^
      DemandState.java DispatcherEntry.java IDemandDispatcher.java DemandDispatcher.java


rem Copying the demand dispatcher class file to the exporte directory ....

copy %CLIENTDIR%\gipsy\DMSException.class %SERVICEDIR%\gipsy
copy %CLIENTDIR%\gipsy\LUSException.class %SERVICEDIR%\gipsy
copy %CLIENTDIR%\gipsy\DemandDispatcherException.class %SERVICEDIR%\gipsy
copy %CLIENTDIR%\gipsy\DemandState.class %SERVICEDIR%\gipsy
copy %CLIENTDIR%\gipsy\DispatcherEntry.class %SERVICEDIR%\gipsy
copy %CLIENTDIR%\gipsy\IDemandDispatcher.class %SERVICEDIR%\gipsy
copy %CLIENTDIR%\gipsy\DemandDispatcher.class %SERVICEDIR%\gipsy