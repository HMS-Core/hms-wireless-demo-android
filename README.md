# HMS Wireless Kit Demo
## Table of Contents

 * [Introduction](#introduction)
 * [Getting Started](#getting-started)
 * [Supported Environments](#supported-environments)
 * [Procedure](#procedure)
 * [Result](#result)
 * [License](#license)
 
## Introduction
The WirelessTestDemo program demonstrates how to build an Android app providing functions including network QoE perception, network prediction and transmission quality feedback.

## Getting Started

1. Check whether the Android studio development environment is ready. Open the sample code project directory with file "build.gradle" in Android Studio. Run TestApp on your divice or simulator with the latest version of Huawei Mobile Service(HMS) installed.
2. Register a [HUAWEI account](https://developer.huawei.com/consumer/en/).
3. Create an app and configure the app information in AppGallery Connect (AGC). 
See details: [HUAWEI Wireless Kit Development Preparation](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides-V5/config-agc-0000001050749961-V5).
4. To build this demo, please first import the demo in the Android Studio (3.x+).
5. Configure the sample code:
     (1) Download the file "agconnect-services.json" of the app on AGC, and add the file to the app root directory(\app) of the demo. 
     (2) Change the value of applicationid in the app-level build.gradle file of the sample project to the package name of your app.
6. Run the sample on your Android device or simulator .

## Supported Environments
   Android SDK Version >= 21 and JDK version >= 1.8 is recommended.

## Procedure
1. Open the app.
2. Touch NETWORKQOE. Then refer to Development Guide. 
3. Touch REPORTAPPQUALITY. Then refer to Development Guide. 
4. Touch NETWORKPREDICTION. Then refer to Development Guide. 

## Result
The sample app enables network QoE perception, signal prediction, and transmission quality feedback.

<img src="https://github.com/HMS-Core/hms-wireless-demo-android/blob/master/image/result_1.JPG" width="200"> <img src="https://github.com/HMS-Core/hms-wireless-demo-android/blob/master/image/result_2.JPG" width="200"> <img src="https://github.com/HMS-Core/hms-wireless-demo-android/blob/master/image/result_3.JPG" width="200">

## License
Wireless Service Connection sample is licensed under the: [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
