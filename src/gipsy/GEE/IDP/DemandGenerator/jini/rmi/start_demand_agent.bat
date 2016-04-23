@rem *********************************************************
@rem **** Batch file to run the GIPSY Demand Dispatcher client
@rem *********************************************************
@rem **** by Emil Vassev
@rem **** Last modification on July 12, 2004
@rem *********************************************************


@rem Runing GIPSY Demand Dispatcher client ....


set JINILIB=G:\jini\gipsy\lib
set CLIENTDIR=G:\jini\client
set POLICYDIR=G:\jini\policy


java -cp %JINILIB%\jini-core.jar;%JINILIB%\jini-ext.jar;%JINILIB%\sun-util.jar;%CLIENTDIR% ^
           -Djava.security.policy=%POLICYDIR%\server.policy ^
           gipsy.DemandDispatcherAgent