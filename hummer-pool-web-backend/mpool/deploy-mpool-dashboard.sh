#!/bin/bash -ilex
. /etc/profile
basepath=$(cd `dirname $0`; pwd)
cd $basepath

PRO_NAME=mpool-dashboard
JAR_NAME=mpool-dashboard-0.0.1-SNAPSHOT.jar
PRO_DIR=mpool-dashboard
JAR_DIR=$basepath/$PRO_DIR/target/${JAR_NAME}

DEPLOY_PATH=/app/webapps/${PRO_NAME}
if [ ! -d "${DEPLOY_PATH}" ]; then
  mkdir -p ${DEPLOY_PATH}
else
  rm -rf ${DEPLOY_PATH}/*
fi
cp ${JAR_DIR} ${DEPLOY_PATH}
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
