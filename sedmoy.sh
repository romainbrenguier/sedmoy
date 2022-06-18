#!/bin/bash

DIR="$( dirname "${BASH_SOURCE[0]}" )"
JVM_ARGS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005
echo $JVM_ARGS
java $JVM_ARGS -jar $DIR/target/sedmoy-1.0-SNAPSHOT.jar "$@"
