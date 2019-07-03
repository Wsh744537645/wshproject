#! /bin/bash
. /etc/profile

basepath=$(cd `dirname $0`; pwd)
cd $basepath
die()
{
    CUR_DATE=$(date +"%Y-%m-%d %H:%M:%S")
    echo "[${CUR_DATE}]: $1"
    exit 1
}
echo "-----------------------"
echo "Start  pool lucky"
echo "-----------------------"
echo

JAR_NAME=mpool-lucky-0.0.1-SNAPSHOT.jar
agrs=
 
nohup java -Duser.timezone=UTC -jar $JAR_NAME $agrs >/dev/null 2>&1 &
