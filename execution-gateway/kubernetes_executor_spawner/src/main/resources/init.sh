#!/bin/bash

BAK_DIR='/sede_backup'
SEDE_DIR='/sede'

# The backup directory should be mounted by Kubernetes without creating it
# manually. When it doesn't exist, there must be an error.
# Todo : Need to check why the directory is not created
mkdir -p ${BAK_DIR}

if [ ! -d "${BAK_DIR}" ]
then
  echo "SEDE backup directory has to exist. DIR: '${BAK_DIR}'"
  exit 1
fi


# If the backup directory is empty, generate the SEDE executor and backup.
# Otherwise copy the backup to SEDE directory.
#Todo: This directory always exists and the process exit without doing anything. Need to check this
#if [ -z "$(ls -A "${BAK_DIR}")" ]
#then

  echo "SEDE backup doesn't exist. Generating..."

  mkdir -p ${SEDE_DIR}
  cd ${SEDE_DIR} || exit 1
  ## a. Clone the bootup scripts in current directory, even if it is not empty
  git init .
  git remote add origin https://github.com/CRC-901-On-the-Fly-Computing/executor-bootup.git -master
  git fetch origin master && git pull origin master

  cp /temp/custom.vars "${SEDE_DIR}/custom.vars"

  ## Making sure all bash scripts will run properly (on the development mode you should run the command by yourself)
  find . -type f -name '*.sh' -exec chmod +x '{}' \;

  ./generate.sh

  echo "Creating SEDE backup..."
  cp -rf ${SEDE_DIR}/* ${BAK_DIR}

#else
#  echo "SEDE backup exists. Copying..."
#
#  mkdir -p ${SEDE_DIR}
#  cp -rf ${BAK_DIR}/* ${SEDE_DIR}
#fi

echo "SEDE initialization finished."
