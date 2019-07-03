#!/bin/bash -ilex
. /etc/profile
basepath=$(cd `dirname $0`; pwd)
cd $basepath

PRO_NAME=mpool-admin
JAR_NAME=mpool-admin-0.0.1-SNAPSHOT.jar
PRO_DIR=./mpool/mpool-admin
JAR_DIR=$basepath/$PRO_DIR/target/${JAR_NAME}

DEPLOY_PATH=/app/webapps/${PRO_NAME}
if [ ! -d "${DEPLOY_PATH}" ]; then
  mkdir -p ${DEPLOY_PATH}
else
  rm -rf ${DEPLOY_PATH}/*
fi
cp ${JAR_DIR} ${DEPLOY_PATH}
fis3 -r ./mpool-admin-ui release -d ${DEPLOY_PATH}/mpool-admin-ui
cp ${PRO_DIR}/*.sh  ${DEPLOY_PATH}

if [ ! -x "${DEPLOY_PATH}/shutdown.sh" ]
then
  chmod 755 "${DEPLOY_PATH}/shutdown.sh"
fi

if [ ! -x "${DEPLOY_PATH}/startup.sh" ]
then
  chmod 755 "${DEPLOY_PATH}/startup.sh"
fi

${DEPLOY_PATH}/shutdown.sh

${DEPLOY_PATH}/startup.sh

