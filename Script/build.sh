#!/bin/bash
echo $(cd "$(dirname "$0")"; pwd)
cd $(cd "$(dirname "$0")"; pwd)
cd ../src
pwd
gradle assembleDebug --info

