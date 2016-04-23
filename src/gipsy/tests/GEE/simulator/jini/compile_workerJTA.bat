@rem *********************************************************
@rem **** Batch file to compile the GIPSY-Simulator JINI client
@rem *********************************************************
@rem **** by Emil Vassev
@rem **** Last modification on April 5, 2005
@rem *********************************************************


set JINILIB=E:\jini2_0_1\lib
set CLIENTDIR=E:\jini\client


rem Compiling GIPSY-Simulator JINI client ....

javac -classpath %JINILIB%\jini-core.jar;%JINILIB%\jini-ext.jar;%JINILIB%\sun-util.jar;%CLIENTDIR% ^
      -d %CLIENTDIR% WorkerJTA.java



