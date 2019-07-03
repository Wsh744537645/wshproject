#!/bin/bash -ilex
. /etc/profile
basepath=$(cd `dirname $0`; pwd)
cd $basepath

PRO_NAME=mpool-account

DEPLOY_PATH=/app/webapps/${PRO_NAME}
if [ ! -d "${DEPLOY_PATH}" ]; then
  mkdir -p ${DEPLOY_PATH}
fi
fis3 -r ./mpool-ui release -d ${DEPLOY_PATH}/mpool-ui
echo 'success!'