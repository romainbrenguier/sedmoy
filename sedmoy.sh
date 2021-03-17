#!/bin/bash

DIR="$( dirname "${BASH_SOURCE[0]}" )"
java -jar $DIR/target/sedmoy-1.0-SNAPSHOT.jar $@
