@rem *********************************************************
@rem **** Batch file to compile the GIPSY-Simulator JINI service
@rem *********************************************************
@rem **** by Emil Vassev
@rem **** Last modification on March 30, 2005
@rem *********************************************************


set JINILIB=E:\jini2_0_1\lib
set SERVICEDIR=E:\jini\service
set CLIENTDIR=E:\jini\client
set JINICODEBASE=E:\jini\service-dl
set JAVAPATH=E:\Java\jdk1.5.0_06




rem Compiling JINI Transport Agent ....

javac -classpath %JINILIB%\jini-core.jar;%JINILIB%\jini-ext.jar;%JINILIB%\sun-util.jar;%SERVICEDIR% ^
      -d %SERVICEDIR% IWorkResult.java IWorkDemand.java IJINITransportAgent.java JINITransportAgent.java

@rem javac -classpath %JINILIB%\jini-core.jar;%JINILIB%\jini-ext.jar;%JINILIB%\sun-util.jar;%SERVICEDIR%;%CLIENTDIR% ^
@rem       -d %SERVICEDIR% IJINITransportAgent.java JINITransportAgent.java



rem Copying the interface class files IWorkResult.class IWorkDemand.class to the client directory ....

copy %SERVICEDIR%\gipsy\IWorkResult.class  %CLIENTDIR%\gipsy
copy %SERVICEDIR%\gipsy\IWorkDemand.class  %CLIENTDIR%\gipsy


rem Copying the proxy class file to the exporte directory ....

copy %SERVICEDIR%\gipsy\JINITransportAgent$JINITransportAgentProxy.class %JINICODEBASE%\gipsy


rem Copying the interface class file to the client directory ....

copy %SERVICEDIR%\gipsy\IJINITransportAgent.class  %CLIENTDIR%\gipsy



rem Generating the RMI stubs ....

rmic -classpath %JAVAPATH%\jre\lib\rt.jar;%JINILIB%\jini-core.jar;%JINILIB%\jini-ext.jar;%JINILIB%\sun-util.jar;%SERVICEDIR% ^
     -d %SERVICEDIR% gipsy.JINITransportAgent.JTABackend



rem Copying the RMI stub class file to the exporte directory ....

copy %SERVICEDIR%\gipsy\JINITransportAgent$JTABackendProtocol.class %JINICODEBASE%\gipsy
copy %SERVICEDIR%\gipsy\JINITransportAgent$JTABackend_Stub.class %JINICODEBASE%\gipsy

 
