#!/usr/bin/env bash
#
# Dependencies:
#   brew cask install docker
#
# Setup:
#   chmod 700 ./build.sh
#
# Usage:
#   ./build.sh [version]
#

# initialize build
./build-init.sh || exit $?

VERSION=$1

if [ -z "${VERSION}" ]; then
  VERSION=latest
fi

REPOSITORY_ROOT=$(cd $(dirname "$0")/.. && pwd)
MODULE_NAME=$(basename $(cd $(dirname "$0") && pwd))

echo "build image... version=${VERSION}"

docker build --build-arg VERSION=${VERSION} \
             --build-arg MODULE_NAME=${MODULE_NAME} \
             -t opklnm102/${MODULE_NAME}:${VERSION} \
             -f ${REPOSITORY_ROOT}/${MODULE_NAME}/Dockerfile ${REPOSITORY_ROOT}
