# P2P金融 APP

![image](https://github.com/xinpengfei520/P2P/blob/master/screenshot/image.gif)

## 一、主要技术点：  

 - 1.使用RadioGroup + Fragment + ViewPager搭建软件框架；  
 - 2.实现带有回弹效果的MyScrollView；  
 - 3.实现流式布局和随机布局效果；  
 - 4.集成第三方的支付(支付宝)；  
 - 5.适配器Adapter的抽取；  
 - 6.实现APP下载更新；  
 - 7.实现九宫格手势图案解锁功能，提高安全性；  
 - 8.实现登录，使用MD5对密码进行加密处理；  
 - 9.自定义圆形进度条效果，并绘制中间文本的百分比显示；  
 - 10.集成第三方Mp_Chart图表库显示各种不同风格的条形图，折线图，饼状图效果；  
 - 11.实现用户头像切换，拍照或从图库中选择，并使用工具实现压缩和圆形处理；  
 - 12.使用ViewPagerIndicator库实现顶部标签页面切换；  
 - 13.使用LoadingPager实现页面加载帧动画效果；  
 - 14.使用TextView实现跑马灯文本效果；  
 - 15.assets目录引用外部字体库到项目中；  
 - 16.使用FastJson解析Json数据；  
 - 17.使用恶汉式单例模式，提供当前应用中所有创建的Activity的管理器，并对外提供添加、删除指定、删除当前、删除所有、返回栈大小的方法；  
 - 18.提供CrashHandler类提供一个全局未被捕获异常时显式捕获的工具类(恶汉式单例)；  
 - 19.对Activity、Fragment实现对公共父类的抽取；  
 - 20.实现对APP界面使用权重、代码、布局、图片等进行适配；
 - 21.集成 Bugly。
 - 22.集成 UETool https://github.com/eleme/UETool

## 二、服务端代码部署

找到**Server**目录中的压缩文件

	将文件解压后导入JavaEE版Eclipse中，然后在工程中右键点击build path--> Configure Build Path 
	
	重新配置本地的Tomcat服务器，如果有就先移除，然后点add Library --> Server RunTime--> 选择本地的Tomcat服务器，
	
	如果JRE配置有问题就同样配置一下本地的JRE环境，add Library --> JRE System Library 选择本地的jre,然后点击ok，
	
	右键点击工程Run As -->run On server ,如果还不行就clean 一下工程然后refresh工程在重新启动就ok

解决方案有三种：

	1、导入项目之前，请确认工作空间编码已设置为utf-8
	window->Preferences->General->Wrokspace->Text file encoding->Other 选择UTF-8

	2、导入后，由于开发环境中JRE以及Tomcat Library名称可能和源代码中的不一致，可能会出现Build Path的错误，
	解决方法如下：
	右键project->Build Path->Configure Build Path->选择Libraries tab页->删除带小红叉的Library->
	点击Add Library->选择JRE System Library(删了Tomcat Library的话就选择Server Runtime)->选择一个JRE后->Finish
	
	3、进入项目包下的.settings目录，找到org.eclipse.wst.common.project.facet.core.xml文件，
	用记事本打开后查看《runtime name="Apache Tomcat v6.0"/》，看是否与你eclipse设置的tomcat版本一致，
	如果不一致，则删除该内容即可。

## 三、IntelliJ IDEA 服务端代码

由于之前的服务端代码是用 Eclipse 写的，现在全部迁移到的 IntelliJ IDEA 中来，如果还在用 Eclipse 的同学建议大家也换过来，可能刚开始不太适应，时间长了，你会发现 IntelliJ IDEA 是真的好用！需要安装教程的戳下面的链接：[安装教程](https://blog.csdn.net/xinpengfei521/article/details/83782062)

下面的是服务端的代码实现 [不断更新ing...]

[https://github.com/xinpengfei520/iMoneyJavaWeb](https://github.com/xinpengfei520/iMoneyJavaWeb)

## TODO
 
 - 处理M动态权限;
 - xiaomi 5手机选择相册设置头像时的Crash；
 - 资产 Tab 顶部 Tab 处理；
 - 沉浸式状态栏；
 - 适配全面屏；
 - 生产环境配置；
 - 完善更多页面内容；
 - 服务端代码打成 war 包部署到阿里云；
 - 将需要的信息建表并存入 MySQL 数据库；
 - 图片存入七牛云图床；
 - 和服务器交互数据加密、校验处理；
 - ...

## Bugs Report

If you find any bug when using this project, please report **[here](https://github.com/xinpengfei520/P2P/issues/new)**. Thanks for helping us making better.

## Change logs

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

![image](https://github.com/xinpengfei520/P2P/blob/master/screenshot/official_account.jpg)

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