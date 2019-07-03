#!/bin/bash -ilex
. /etc/profile
basepath=$(cd `dirname $0`; pwd)
cd $basepath

PRO_NAME=mpool-account
JAR_NAME=mpool-account-0.0.1-SNAPSHOT.jar
PRO_DIR=./mpool/mpool-account
JAR_DIR=$basepath/$PRO_DIR/target/${JAR_NAME}

DEPLOY_PATH=/app/webapps/${PRO_NAME}
if [ ! -d "${DEPLOY_PATH}" ]; then
  mkdir -p ${DEPLOY_PATH}
else
  rm -rf ${DEPLOY_PATH}/*
fi
cp ${JAR_DIR} ${DEPLOY_PATH}
fis3 -r ./mpool-ui release -d ${DEPLOY_PATH}/mpool-ui
cp ${PRO_DIR}/*.sh  ${DEPLOY_PATH}

if [ ! -x "/app/webapps/mpool-account/shutdown-account.sh" ]
then
  chmod 755 "/app/webapps/mpool-account/shutdown-account.sh"
fi

if [ ! -x "/app/webapps/mpool-account/startup-account.sh" ]
then
  chmod 755 "/app/webapps/mpool-account/startup-account.sh"
fi

/app/webapps/mpool-account/shutdown-account.sh

/app/webapps/mpool-account/startup-account.sh
