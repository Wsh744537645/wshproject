#! /bin/bash
basepath=$(cd `dirname $0`; pwd)

echo "-----------------------"
echo "Stop  mpool share"
echo "-----------------------"
echo
JAR_NAME=mpool-pool-share-0.0.1-SNAPSHOT.jar

pids=$(ps -ef | grep "${JAR_NAME}" | grep -v "grep"| awk -F ' ' '{print $2}')

if [ "X$pids" != "X" ]; then
    kill -9 $pids
    echo "Stop mpool share success."
else
    echo "mpool share already stopped."
fi
