#! /bin/bash
basepath=$(cd `dirname $0`; pwd)
cd $basepath
die()
{
    CUR_DATE=$(date +"%Y-%m-%d %H:%M:%S")
    echo "[${CUR_DATE}]: $1"
    exit 1
}
echo "-----------------------"
echo "Start  epool-account-multiple"
echo "-----------------------"
echo

JAR_NAME=mpool-account-multiple-0.0.1-SNAPSHOT.jar
#agrs=--spring.resources.static-locations=file:mpool-ui/,classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/,classpath:/public/ 
nohup java -Duser.timezone=UTC -jar $JAR_NAME >/dev/null 2>&1 &
