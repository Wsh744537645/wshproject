#! /bin/bash
basepath=$(cd `dirname $0`; pwd)

echo "-----------------------"
echo "Stop  mpool-account"
echo "-----------------------"
echo
JAR_NAME=mpool-account-0.0.1-SNAPSHOT.jar

pids=$(ps -ef | grep "${JAR_NAME}" | grep -v "grep"| awk -F ' ' '{print $2}')

if [ "X$pids" != "X" ]; then
    kill -9 $pids
    echo "Stop mpool-account success."
else
    echo "mpool-account already stopped."
fi
