rem This is a quick batch file to make this module compile under Windows.
rem If this quick hackery is not updated, person to blame: Serguei
rem
rem $Header: /cvsroot/gipsy/gipsy/src/gipsy/GEE/IDP/DemandGenerator/threaded/make.bat,v 1.1 2003/06/18 14:13:39 mokhov Exp $
rem

@echo off

rem set %BASE_GIPSY_DIR="../../../../.."

javac -sourcepath ..\..\..\..\.. *.java > compile.log

@echo FIXME: Run and Clean options aren't here yet :-(

rem EOF
