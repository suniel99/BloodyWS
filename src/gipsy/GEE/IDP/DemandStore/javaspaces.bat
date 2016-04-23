@rem **** This batch file starts the JavaSpaces run-time service   
@rem **** All the supporting services are run first  
@rem ****
@rem ****
@rem **** The run of a JavaSpaces service depends on having a Jini infrastructure in place.
@rem **** Hence, before start a JavaSpaces service, we need to start the following run-time services: 
@rem **** 
@rem **** 1. An HTTP server, which is used to download code to JavaSpaces clients.
@rem **** 
@rem **** 2. An RMI Activation Daemon, which takes care of managing the states of services, 
@rem ****    for instance restarting crashed services, or deactivating and reactivating services. 
@rem **** 
@rem **** 3. A Lookup Service, which allows clients to look up and find the Jini services that are 
@rem ****    currently available on the local network.
@rem **** 
@rem **** 4. A Transaction Manager, which is needed if our JavaSpaces applications make use of transactions.
@rem **** 
@rem **** Once these services are up and running, you can start: 
@rem **** 5. A JavaSpaces Service. 



@rem **** Set up script variables ****

set JINILIB=G:\jini\gipsy\lib
set JINICODEBASE=G:\jini\service-dl
set HTTPPORT=8085
set POLICYDIR=G:\jini\policy



@rem **** Run Java HTTP server ****

start java -jar %JINILIB%\tools.jar -dir %JINICODEBASE% -verbose -port %HTTPPORT%



@rem **** Run RMI Activation Deamon ****

start rmid -J-Djava.security.policy=%POLICYDIR%\policy



@rem **** Run Reggie Lookup Service ****

start java -Djava.security.policy=config\start.policy ^
      -jar lib\start.jar ^
      config\start-reggie.config



@rem **** Run Transaction Manager Service ****

rem start  java -Djava.security.policy=config/jsk-all.policy ^
rem            -jar lib/start.jar ^
rem            config/start-persistent-jrmp-mahalo.config



@rem **** Run JavaSpaces Service ****

start  java -Djava.security.policy=config/outrigger-all.policy ^
       -jar lib/start.jar ^
       config/start-transient-jrmp-outrigger.config

