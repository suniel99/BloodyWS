@rem *********************************************************
@rem **** Batch file to compile the GIPSY Demand Dispatcher
@rem *********************************************************
@rem **** by Emil Vassev
@rem **** Last modification on June 11, 2004
@rem *********************************************************


set JINILIB=G:\jini\gipsy\lib
set CLIENTDIR=G:\jini\client

rem Compiling GIPSY Demand Dispatcher ....

javac -classpath %JINILIB%\jini-core.jar;%JINILIB%\jini-ext.jar;%JINILIB%\sun-util.jar;%CLIENTDIR% ^
      -d %CLIENTDIR% ^
      WorkResult.java WorkTask.java DispatcherEntry.java LUSException.java DemandDispatcherException.java ^
      IDemandDispatcher.java DemandDispatcher.java
