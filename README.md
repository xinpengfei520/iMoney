# iMoney 金融 APP

[apk下载](https://www.pgyer.com/android_app_iMoney)

<table>
    <tr>
        <td><img src="https://github.com/xinpengfei520/iMoney/blob/master/screenshot/image1.png" border=0 width="216" height="468"></td>
        <td><img src="https://github.com/xinpengfei520/iMoney/blob/master/screenshot/image2.png" border=0 width="216" height="468"></td>
        <td><img src="https://github.com/xinpengfei520/iMoney/blob/master/screenshot/image3.png" border=0 width="216" height="468"></td>
    </tr>
</table>

## 一、主要技术点：  

 - 1.使用 RadioGroup + Fragment + ViewPager 搭建软件框架；  
 - 2.实现带有回弹效果的MyScrollView；  
 - 3.实现流式布局和随机布局效果；  
 - 4.集成第三方的支付(支付宝)；  
 - 5.适配器 Adapter 的抽取；  
 - 6.实现 APP 下载更新；  
 - 7.实现九宫格手势图案解锁功能，提高安全性；  
 - 8.实现登录，使用 MD5 对密码进行加密处理；  
 - 9.自定义圆形进度条效果，并绘制中间文本的百分比显示；  
 - 10.集成第三方 Mp_Chart 图表库显示各种不同风格的条形图，折线图，饼状图效果；  
 - 11.实现用户头像切换，拍照或从图库中选择，并使用工具实现压缩和圆形处理；  
 - 12.使用 ViewPagerIndicator 库实现顶部标签页面切换；  
 - 13.使用 LoadingPager 实现页面加载帧动画效果；  
 - 14.使用 TextView 实现跑马灯文本效果；  
 - 15.assets 目录引用外部字体库到项目中；  
 - 16.使用 FastJson 解析 Json 数据；  
 - 17.使用恶汉式单例模式，提供当前应用中所有创建的 Activity 的管理器，并对外提供添加、删除指定、删除当前、删除所有、返回栈大小的方法；  
 - 18.提供 CrashHandler 类提供一个全局未被捕获异常时显式捕获的工具类(恶汉式单例)；  
 - 19.对 Activity、Fragment 实现对公共父类的抽取；  
 - 20.实现对 APP 界面使用权重、代码、布局、图片等进行适配；
 - 21.集成 Bugly；
 - 22.集成 UETool https://github.com/eleme/UETool；
 - 23.集成 GrowingIO sdk；（https://www.growingio.com/projects/rREq8vyR/install_sdk）

## 二、IntelliJ IDEA 服务端代码

由于之前的服务端代码是用 Eclipse 写的，现在全部迁移到的 IntelliJ IDEA 中来，如果还在用 Eclipse 的同学建议大家也换过来，可能刚开始不太适应，时间长了，你会发现 IntelliJ IDEA 是真的好用！需要安装教程的戳下面的链接：[安装教程](https://blog.csdn.net/xinpengfei521/article/details/83782062)

下面的是服务端的代码实现 [不断更新ing...]

[https://github.com/xinpengfei520/iMoneyJavaWeb](https://github.com/xinpengfei520/iMoneyJavaWeb)

## TODO
 
 - 生产环境配置；
 - 完善更多页面内容；
 - 将需要的信息建表并存入 MySQL 数据库；
 - 和服务器交互数据加密、校验处理；
 - ...

## Bugs Report

If you find any bug when using this project, please report **[here](https://github.com/xinpengfei520/P2P/issues/new)**. Thanks for helping us making better.

## Change logs

### 1.4.5

 - update on 2019-09-25 
 - bugs fix.
 - Integrated GrowingIO sdk.
 - optimized code.
 
### 1.3.0

 - update on 2018-09-02 bugs fix.
 - Custom TitleBarLayout view.
 - reconsituation login page whith MVP.
 - optimized code.

### 1.2.0

 - update on 2018-06-03 bugs fix.
 - add http、common module.
 - add config.gradle file.

### 1.1.0

 - update on 2017-10-01 bugs fix.
 - adapter AS 3.0

### 1.0.0

 - update on 2016-11-28 first commit.

## 更多

如果你想学习更多关于 Android 的技术或者有问题想与我交流，请扫描下面的微信公众号后点击联系我 -> 个人微信，添加后可与我交流。

![image](screenshot/official_account.jpg)

## LICENSE

```
   Copyright (C) 2016 x-sir

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