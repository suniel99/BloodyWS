@rem *********************************************************
@rem **** Batch file to compile the GIPSY Demand Dispatcher Client
@rem *********************************************************
@rem **** by Emil Vassev
@rem **** Last modification on July 12, 2004
@rem *********************************************************


set JINILIB=G:\jini\gipsy\lib
set CLIENTDIR=G:\jini\client


rem Compiling GIPSY Demand Dispatcher ....

javac -classpath %JINILIB%\jini-core.jar;%JINILIB%\jini-ext.jar;%JINILIB%\sun-util.jar;%CLIENTDIR% ^
      -d %CLIENTDIR% DemandDispatcherClient.java



