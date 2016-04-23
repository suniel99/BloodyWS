set GIPSYROOT=../../
set GIPSYLIB=%GIPSYROOT%/lib
set GIPSYCLASSPATH=%GIPSYROOT%/bin

java -cp %GIPSYCLASSPATH%;%GIPSYLIB%/marf.jar;%GIPSYLIB%/start.jar;%GIPSYLIB%/tools.jar;%GIPSYLIB%/sun-util.jar;%GIPSYLIB%/jini-core.jar;%GIPSYLIB%/jini-ext.jar;%GIPSYLIB%/reggie-dl.jar;%GIPSYLIB%/outrigger-dl.jar;%GIPSYLIB%/mahalo-dl.jar;%GIPSYLIB%/jms.jar;%GIPSYLIB%/imq.jar -Xms768m -Xmx768m -Xss64k gipsy.GEE.GEE --node=RegularNode.config


pause