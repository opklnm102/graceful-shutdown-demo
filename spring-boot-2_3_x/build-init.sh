#!/usr/bin/env bash

if ! [ -x "$(command -v docker)" ]; then
  echo "docker not install"
  exit 1
fi

trap "exit 0" SIGHUP SIGINT SIGTERM
