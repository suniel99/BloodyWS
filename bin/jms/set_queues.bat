cd mq/bin/

set JREHOME="C:\Program Files\Java\Jre6"

imqcmd create dst -t q -n pending -b localhost:7676 -u admin -passfile ../../password.txt -jrehome %JREHOME%
imqcmd create dst -t q -n inprocess -b localhost:7676 -u admin -passfile ../../password.txt -jrehome %JREHOME%
imqcmd create dst -t q -n computed -b localhost:7676 -u admin -passfile ../../password.txt -jrehome %JREHOME%

pause