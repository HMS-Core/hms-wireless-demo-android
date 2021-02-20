# 华为无线传输服务示例代码
## 目录

 * [简介](#简介)
 * [快速入门](#快速入门)
 * [环境要求](#环境要求)
 * [操作步骤](#操作步骤)
 * [运行结果](#运行结果)
 * [许可证](#许可证)
 
## 简介
WirelessTestDemo演示程序展示了如何构建一个Android应用以提供网络QoE信息感知、网络预测、传输质量反馈等功能。

## 快速入门

1. 检查Android Studio开发环境是否准备就绪。在Android Studio中打开文件“build.gradle”所在的示例代码工程目录。在已安装最新Huawei Mobile Service(HMS)的设备或模拟器上运行TestApp。
2. 注册[华为账号](https://developer.huawei.com/consumer/cn/)。
3. 创建应用，在AGC (AppGallery Connect)中配置应用信息。详情请参考[HUAWEI Wireless Kit 开发准备](https://developer.huawei.com/consumer/cn/doc/development/HMSCore-Guides-V5/config-agc-0000001050749961-V5)。
4. 构建此demo前，请先将demo导入Android Studio（3.X版本或以上）。
5. 配置样例代码：
     (1) 在AppGallery Connect上下载该APP的agconnect-services.json文件，并将文件存储到demo的app根目录（\app）下。 
     (2) 打开demo的app级build.gradle文件，并将applicationid修改为应用的包名。
6. 在您的Android设备或模拟器上运行demo。

## 环境要求
   建议使用Android SDK 21版本或以上，JDK 1.8版本或以上。

## 操作步骤
1. 打开应用。
2. 点击“NETWORKQOE”。详情请参考[开发步骤](https://developer.huawei.com/consumer/cn/codelab/HMSWirelessKit/index.html#4)。 
3. 点击“REPORTAPPQUALITY”。详情请参考[开发步骤](https://developer.huawei.com/consumer/cn/doc/development/HMSCore-Guides/client-dev-procedure-0000001051075068)。
4. 点击“NETWORKPREDICTION”。详情请参考[开发步骤](https://developer.huawei.com/consumer/cn/doc/development/HMSCore-Guides/client-dev-procedure-0000001051075068)。

## 运行结果
启用网络QoE信息感知、网络预测、传输质量反馈功能。

<img src="https://github.com/HMS-Core/hms-wireless-demo-android/blob/master/image/result_1.JPG" width="200"> <img src="https://github.com/HMS-Core/hms-wireless-demo-android/blob/master/image/result_2.JPG" width="200"> <img src="https://github.com/HMS-Core/hms-wireless-demo-android/blob/master/image/result_3.JPG" width="200">

## 更多详情
如需了解更多HMS Core相关信息，请前往[Reddit](https://www.reddit.com/r/HuaweiDevelopers/)社区获取HMS Core最新资讯，参与开发者讨论。

如您对示例代码使用有疑问，请前往：
- [Stack Overflow](https://stackoverflow.com/questions/tagged/huawei-mobile-services)提问，上传问题时请打上huawei-mobile-services标签。
- [华为开发者论坛](https://developer.huawei.com/consumer/cn/forum/block/hms-core)，获得更多意见与建议。

如您在运行示例代码时出现错误，请到GitHub提交[issue](https://github.com/HMS-Core/hms-push-serverdemo-go/issues)或[Pull Request](https://github.com/HMS-Core/hms-push-serverdemo-go/pulls)。

## 许可证
Wireless Service Connection经过[Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0)授权许可。
