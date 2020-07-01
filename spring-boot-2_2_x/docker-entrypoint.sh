#!/usr/bin/env bash

set -eo pipefail

if [ -n "${XMS}" ]; then
  JAVA_OPTS="${JAVA_OPTS} -Xms${XMS}m"
fi

if [ -n "${XMX}" ]; then
  JAVA_OPTS="${JAVA_OPTS} -Xmx${XMX}m"
fi

export JAVA_OPTS="${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -Dfile.encoding=UTF-8"

echo "java ${JAVA_OPTS} -jar app.jar"

exec java ${JAVA_OPTS} -jar app.jar
