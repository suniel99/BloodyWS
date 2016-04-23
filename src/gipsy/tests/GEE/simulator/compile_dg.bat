@rem *********************************************************
@rem **** Batch file to compile the GIPSY- GTA Simulator
@rem *********************************************************
@rem **** by Emil Vassev
@rem **** Last modification on June 09, 2007
@rem *********************************************************


set JINILIB=E:\jini2_0_1\lib
set CLIENTDIR=E:\jini\client


rem Compiling GIPSY-Simulator JINI client ....

javac -classpath %JINILIB%\jini-core.jar;%JINILIB%\jini-ext.jar;%JINILIB%\sun-util.jar;%CLIENTDIR% ^
      -d %CLIENTDIR% gipsy\DemandIDPool.java gipsy\DemandPool.java gipsy\ResultPool.java gipsy\DemandClassPool.java gipsy\Semaphore.java gipsy\GlobalDef.java gipsy\ProfileFilter.java ^
       gipsy\ProfileDialog.java gipsy\DGTDialog.java gipsy\DemandFactory.java gipsy\DemandSender.java gipsy\ResultReceiver.java gipsy\ResultProcessor.java gipsy\GUIThread.java gipsy\DGT.java



