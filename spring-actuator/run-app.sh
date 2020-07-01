#!/usr/bin/env bash

# parsing arguments
while getopts ":p:v:" opt; do
  case ${opt} in
    p)
      PORT=${OPTARG}
      if ! [[ "${PORT}" =~ ^-?[0-9]+$ ]]; then
        echo "invalid port"
        exit 1
      fi
      ;;
    v)
      VERSION=${OPTARG}
      ;;
    \?)
      echo "invalid option: -${OPTARG}" >&2
      exit 1
      ;;
    :)
      echo "Option -${OPTARG} requires an argument" >&2
      exit 1
      ;;
  esac
done

if [ -z "${VERSION}" ]; then
  VERSION=latest
fi

if [ -z "${PORT}" ]; then
  PORT=8080
fi

echo -n "Rebuild the app before launching it? [y/n]? "
read answer

if [ "${answer}" == "y" ] || [ "${answer}" == "Y" ]; then
  echo "Building the app..."
  echo ""

  ./build.sh ${VERSION}

  EXIT_CODE=$?

  if [ ${EXIT_CODE} != 0 ]; then
    echo "Build failed!"
    exit ${EXIT_CODE}
  fi

elif [ "${answer}" == "n" ] || [ "${answer}" == "N" ]; then
  echo "Skip the build steps!"
  echo ""
else
  echo "Y(y) or N(n) plz~"
  echo ""
  exit 1
fi

MODULE_NAME=$(basename $(cd $(dirname "$0") && pwd))

echo "Launching the app... version=${VERSION}"
echo ""

# run app
docker run -p ${PORT}:8080 opklnm102/${MODULE_NAME}:${VERSION}
