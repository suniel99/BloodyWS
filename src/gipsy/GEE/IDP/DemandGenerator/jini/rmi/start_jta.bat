@rem Batch file to run GIPSY-Simulator JINI service

cd ../../../../../..
set GIPSYLIB=D:\workspace\gipsy\lib
set GIPSYCLASSES=D:\workspace\gipsy\bin
set POLICYDIR=gipsy/GEE/IDP/config

java -cp %GIPSYLIB%\jini-core.jar;%GIPSYLIB%\jini-ext.jar;%GIPSYLIB%\marf.jar;%GIPSYCLASSES% ^
           -Djava.security.policy=%POLICYDIR%/jini.policy ^
           gipsy.GEE.IDP.DemandGenerator.jini.rmi.JINITransportAgent
