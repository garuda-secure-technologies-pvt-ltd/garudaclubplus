@echo off

REM    Openbravo POS is a point of sales application designed for touch screens.
REM    Copyright (C) 2008 Openbravo, S.L.
REM    http://sourceforge.net/projects/openbravopos
REM
REM    This program is free software; you can redistribute it and/or modify
REM    it under the terms of the GNU General Public License as published by
REM    the Free Software Foundation; either version 2 of the License, or
REM    (at your option) any later version.
REM
REM    This program is distributed in the hope that it will be useful,
REM    but WITHOUT ANY WARRANTY; without even the implied warranty of
REM    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
REM    GNU General Public License for more details.
REM
REM    You should have received a copy of the GNU General Public License
REM    along with this program; if not, write to the Free Software
REM    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  

set DIRNAME=%~dp0

set CP="%DIRNAME%BCI.jar"

set CP=%CP%;"%DIRNAME%lib/commons-codec-1.4.jar"
set CP=%CP%;"%DIRNAME%lib/commons-codec-1.4-javadoc.jar"
set CP=%CP%;"%DIRNAME%lib/commons-codec-1.4-sources.jar"
set CP=%CP%;"%DIRNAME%lib/commons-io-1.1.jar"
set CP=%CP%;"%DIRNAME%lib/commons-io-1.4.jar"
set CP=%CP%;"%DIRNAME%lib/commons-io-1.4-javadoc.jar"
set CP=%CP%;"%DIRNAME%lib/commons-io-1.4-sources.jar"
set CP=%CP%;"%DIRNAME%lib/dpfpenrollment.jar"
set CP=%CP%;"%DIRNAME%lib/dpfpverification.jar"
set CP=%CP%;"%DIRNAME%lib/dpotapi.jar"
set CP=%CP%;"%DIRNAME%lib/dpotjni.jar"
set CP=%CP%;"%DIRNAME%lib/jdun.jar"
set CP=%CP%;"%DIRNAME%lib/jxl.jar"
set CP=%CP%;"%DIRNAME%lib/poi-3.0.1-FINAL-20070705.jar"
set CP=%CP%;"%DIRNAME%lib/RXTXcomm.jar"
set CP=%CP%;"%DIRNAME%lib/sqljdbc.jar"
set CP=%CP%;"%DIRNAME%lib/xmlsec-2.0.jar"

start /B javaw -cp %CP% -Djava.library.path="%DIRNAME%lib/Windows/i368-mingw32" -Ddirname.path="%DIRNAME%./" bci.BCIMainFrame %1
