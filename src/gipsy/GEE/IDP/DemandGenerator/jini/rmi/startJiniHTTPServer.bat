cd ../../../../../..

set RUNTIME_JAR="D:\Program Files\Java\jre6\lib\rt.jar"
set JINIHOME_BACKSLASH="D:\Program Files\jini2_1"
set JINI_CLASSPATH=.;%RUNTIME_JAR%;%JINIHOME_BACKSLASH%\lib\jini-core.jar;%JINIHOME_BACKSLASH%\lib\jini-ext.jar;%JINIHOME_BACKSLASH%\lib\reggie.jar;%JINIHOME_BACKSLASH%\lib-dl\reggie-dl.jar;%JINIHOME_BACKSLASH%\lib\mahalo.jar;%JINIHOME_BACKSLASH%\lib-dl\mahalo-dl.jar;%JINIHOME_BACKSLASH%\lib\outrigger.jar;%JINIHOME_BACKSLASH%\lib-dl\outrigger-dl.jar;%JINIHOME_BACKSLASH%\lib\tools.jar;%JINIHOME_BACKSLASH%\lib\sun-util.jar;

java -jar -classpath %JINI_CLASSPATH% %JINIHOME_BACKSLASH%\lib\tools.jar -port 8085 -dir . -verbose

