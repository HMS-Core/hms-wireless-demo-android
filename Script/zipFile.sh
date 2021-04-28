#!/bin/bash
echo $(cd "$(dirname "$0")"; pwd)
cd $(cd "$(dirname "$0")"; pwd)
cd ../..
pwd
zip -r wireless_demo/demoCode.zip \
wireless_demo/image \
wireless_demo/src/app/src \
wireless_demo/src/app/build.gradle \
wireless_demo/src/app/proguard-rules.pro \
wireless_demo/src/gradle \
wireless_demo/src/build.gradle \
wireless_demo/src/gradle.properties \
wireless_demo/src/gradlew \
wireless_demo/src/gradlew.bat \
wireless_demo/src/settings.gradle \
wireless_demo/LICENSE \
wireless_demo/README.md

