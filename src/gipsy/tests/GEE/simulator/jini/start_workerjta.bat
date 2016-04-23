@rem Batch file to run WorkerJTA

cd ../../../../..
set GIPSYLIB=D:\workspace\gipsy\lib
set GIPSYCLASSES=D:\workspace\gipsy\bin
set POLICYDIR=gipsy/GEE/IDP/config

java -cp %GIPSYLIB%\jini-core.jar;%GIPSYLIB%\jini-ext.jar;%GIPSYLIB%\marf.jar;%GIPSYLIB%\jms.jar;%GIPSYLIB%\fscontext.jar;%GIPSYLIB%\imq.jar;%GIPSYCLASSES% ^
           -Djava.security.policy=%POLICYDIR%/jini.policy ^
           gipsy.tests.GEE.simulator.jini.WorkerJTA