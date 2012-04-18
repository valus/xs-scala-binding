#!/bin/sh


# Internal options, always specified
INTERNAL_OPTS="-Dfile.encoding=UTF-8 -Xmx512M -XX:MaxPermSize=256m"

# Add 64bit specific option
exec java -version 2>&1 | grep -q "64-Bit" && INTERNAL_OPTS="${INTERNAL_OPTS} -XX:+UseCompressedOops -XX:ReservedCodeCacheSize=128m"

# Default options, if nothing is specified
DEFAULT_OPTS=""

cd `dirname $0`

exec java ${INTERNAL_OPTS} -jar project/sbt-launch.jar "$@"