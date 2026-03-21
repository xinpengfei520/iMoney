# iMoney Financial APP

**[中文](README.md) | English**

![Version](https://img.shields.io/github/v/release/xinpengfei520/iMoney?label=version)
![Stars](https://img.shields.io/github/stars/xinpengfei520/iMoney)
![Forks](https://img.shields.io/github/forks/xinpengfei520/iMoney)
![License](https://img.shields.io/github/license/xinpengfei520/iMoney)

<table>
    <tr>
        <td><img src="https://github.com/xinpengfei520/iMoney/blob/master/screenshot/image1.png" border=0 width="216" height="468"></td>
        <td><img src="https://github.com/xinpengfei520/iMoney/blob/master/screenshot/image2.png" border=0 width="216" height="468"></td>
        <td><img src="https://github.com/xinpengfei520/iMoney/blob/master/screenshot/image3.png" border=0 width="216" height="468"></td>
    </tr>
</table>

## Star History

[![Star History Chart](https://api.star-history.com/svg?repos=xinpengfei520/iMoney&type=Date)](https://star-history.com/#xinpengfei520/iMoney&Date)

## 1. Key Technical Highlights

 - 1. App framework built with RadioGroup + Fragment + ViewPager;
 - 2. Custom MyScrollView with bounce-back effect;
 - 3. Flow layout and random layout implementations;
 - 4. Third-party payment integration (Alipay);
 - 5. Common Adapter abstraction and extraction;
 - 6. In-app download and update functionality;
 - 7. Gesture pattern lock (9-dot grid) for enhanced security;
 - 8. Login with MD5 password encryption;
 - 9. Custom circular progress bar with centered percentage text;
 - 10. Integrated MPAndroidChart library for bar charts, line charts, and pie charts;
 - 11. User avatar switching — capture from camera or pick from gallery, with compression and circular cropping;
 - 12. ViewPagerIndicator for top tab page switching;
 - 13. LoadingPager for page loading frame animations;
 - 14. Marquee text effect using TextView;
 - 15. External font library loaded from the assets directory;
 - 16. JSON data parsing with FastJson;
 - 17. Eager singleton ActivityManager for managing all Activities in the app, providing add, remove specific, remove current, remove all, and stack size methods;
 - 18. CrashHandler class (eager singleton) for global uncaught exception handling;
 - 19. Base class extraction for Activity and Fragment;
 - 20. Screen adaptation using weight, code, layout, and image resources;
 - 21. Bugly crash reporting integration;
 - 22. UETool integration: https://github.com/eleme/UETool;
 - 23. GrowingIO SDK integration;
 - 24. AndResGuard integration: https://github.com/shwenzhang/AndResGuard

## 2. Server-side Code (IntelliJ IDEA)

The server-side code has been fully migrated from Eclipse to IntelliJ IDEA.

Server-side implementation [continuously updated...]:

[https://github.com/xinpengfei520/iMoneyJavaWeb](https://github.com/xinpengfei520/iMoneyJavaWeb)

## TODO

 - Store required data in MySQL database tables;
 - Encrypt and verify data exchanged with the server;
 - Integrate Pgyer feedback;
 - Tax calculator (inspired by Xiaomi Calculator);
 - Add bookkeeping feature;

## Bugs Report

If you find any bug when using this project, please report **[here](https://github.com/xinpengfei520/P2P/issues/new)**. Thanks for helping us making better.

## Change logs

### 1.4.6

 - Integrated AndResGuard;
 - Optimized code.

### 1.4.5

 - Updated on 2019-09-25
 - Bug fixes.
 - Integrated GrowingIO SDK.
 - Optimized code.

### 1.3.0

 - Updated on 2018-09-02, bug fixes.
 - Custom TitleBarLayout view.
 - Refactored login page with MVP pattern.
 - Optimized code.

### 1.2.0

 - Updated on 2018-06-03, bug fixes.
 - Added http and common modules.
 - Added config.gradle file.

### 1.1.0

 - Updated on 2017-10-01, bug fixes.
 - Adapted to Android Studio 3.0.

### 1.0.0

 - Updated on 2016-11-28, first commit.

## Contact

If you want to learn more about Android development or have questions, scan the WeChat Official Account QR code below, then click "Contact Me" -> "Personal WeChat" to connect.

![image](screenshot/official_account.jpg)

## LICENSE

```
   Copyright (C) 2016 Vance

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
