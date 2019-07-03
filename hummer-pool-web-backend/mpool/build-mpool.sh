#!/bin/bash -ilex
. /etc/profile
basepath=$(cd `dirname $0`; pwd)
cd $basepath
mvn clean package $1
if [ $? != 0 ]; then
    die "Build ${PRO_NAME} project failed."
fi
