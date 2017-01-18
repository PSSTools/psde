#!/bin/sh

uname_o=`uname -o`
is_mingw=`uname | sed -e 's/MINGW.*$/1/'`

if test "$is_mingw" = "1"; then
  os=win32
  ws=win32
  eclipse=eclipsec
elif test "$uname_o" = "Cygwin"; then
  os=win32
  ws=win32
  eclipse=eclipsec
else
  os=linux
  ws=gtk
  eclipse=eclipse
fi

#    -verbose \


$ECLIPSE_XTEXT_HOME/$eclipse \
    -nosplash -application org.eclipse.ant.core.antRunner \
    --launcher.suppressErrors \
    -buildfile build.xml      \
    -Dos=$os -Dws=$ws -Darch=x86_64 $extra_defs build

