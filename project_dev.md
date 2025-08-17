# iMoney 项目技术开发架构文档

## 1. 项目概述

iMoney 是一个基于 Android 平台的金融类移动应用，主要提供投资理财、资产管理等金融服务功能。项目采用现代化的 Android 开发技术栈，支持多语言国际化，集成了多个第三方服务SDK。

## 2. 技术栈和依赖

### 2.1 核心技术栈
- **开发语言**: Kotlin + Java 混合开发
- **架构模式**: MVP (Model-View-Presenter)
- **UI框架**: Fragment + ViewPager
- **构建工具**: Gradle (Kotlin DSL)
- **最低支持**: Android API 21 (Android 5.0)
- **目标版本**: Android API 29 (Android 10)

### 2.2 主要依赖库

#### UI组件
- `material`: Material Design 组件库
- `constraintlayout`: 约束布局
- `banner`: 轮播图组件
- `MagicIndicator`: 指示器组件
- `MPAndroidChart`: 图表库

#### 网络和数据
- `okhttp`: HTTP客户端
- `retrofit`: REST API客户端
- `gson`: JSON解析
- `rxjava`: 响应式编程
- `picasso`: 图片加载

#### 工具库
- `autosize`: 屏幕适配
- `XXPermissions`: 权限管理
- `multidex`: 多dex支持
- `LeakCanary`: 内存泄漏检测
- `UETool`: UI调试工具

## 3. 项目架构

### 3.1 整体架构
```
iMoney/
├── app/                    # 主应用模块
│   ├── src/main/
│   │   ├── java/com/xpf/p2p/
│   │   ├── res/
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── okla-library/          # 自定义HTTP库模块
│   ├── src/main/java/com/xpf/http/
│   └── build.gradle
├── config.gradle          # 全局配置
└── build.gradle.kts       # 根项目构建脚本
```

### 3.2 MVP架构实现
- **Model**: 数据层，负责数据获取和业务逻辑
- **View**: 视图层，负责UI展示和用户交互
- **Presenter**: 表示层，连接Model和View，处理业务逻辑

## 4. 模块结构

### 4.1 主应用模块 (app)
```
com.xpf.p2p/
├── activity/              # Activity类
├── adapter/               # 适配器类
├── base/                  # 基础类
├── constants/             # 常量定义
├── entity/                # 实体类
├── fragment/              # Fragment类
├── jpush/                 # 极光推送相关
├── ui/                    # UI模块
│   ├── login/            # 登录模块
│   ├── main/             # 主页模块
│   └── multilanguage/    # 多语言模块
├── utils/                 # 工具类
│   └── pay/              # 支付相关工具
├── widget/                # 自定义控件
└── uetool/                # UI调试工具
```

### 4.2 HTTP库模块 (okla-library)
```
com.xpf.http/
├── OklaClient.java        # HTTP客户端主类
├── core/                  # 核心功能
│   ├── Okla.java         # 主要API类
│   ├── callback/         # 回调接口
│   └── request/          # 请求相关
├── httpdns/              # DNS解析
├── logger/               # 日志工具
└── utils/                # 工具类
```

## 5. 主要功能模块

### 5.1 核心功能
- **首页 (HomeFragment2)**: 展示主要金融信息和产品
- **投资 (InvestFragment)**: 投资理财功能
- **资产 (MeFragment)**: 个人资产管理
- **更多 (MoreFragment)**: 其他功能和设置

### 5.2 基础功能
- **用户认证**: 登录、注册、权限管理
- **多语言支持**: 国际化和本地化
- **数据统计**: 用户行为分析
- **消息推送**: 实时消息通知
- **支付集成**: 第三方支付服务

## 6. 开发环境配置

### 6.1 环境要求
- **JDK**: 17+
- **Android Studio**: 最新稳定版
- **Gradle**: 8.0+
- **Kotlin**: 1.8+

### 6.2 构建配置
- **编译SDK**: 继承自rootProject配置
- **最小SDK**: API 21
- **目标SDK**: API 29
- **支持架构**: armeabi-v7a, arm64-v8a

### 6.3 环境变量
项目支持多环境配置：
- **开发环境** (dev)
- **测试环境** (test)
- **生产环境** (prod)

## 7. 构建和部署

### 7.1 构建类型
- **Debug**: 开发调试版本，未启用混淆
- **Release**: 发布版本，启用代码混淆和资源压缩

### 7.2 签名配置
- 支持Debug和Release两种签名配置
- 使用Gradle任务自动化APK安装和应用启动

### 7.3 版本管理
- 通过Git自动获取版本号
- 支持自动化版本发布流程

## 8. 第三方集成

### 8.1 数据统计和分析
- **GrowingIO**: 用户行为分析和数据统计
- **Bugly**: 崩溃监控和异常上报

### 8.2 消息推送
- **极光推送 (JPush)**: 实时消息推送服务

### 8.3 支付服务
- **支付宝SDK**: 支付宝支付集成
- **微信SDK**: 微信支付集成

### 8.4 其他服务
- **阿里云HTTPDNS**: DNS解析优化
- **友盟统计**: 应用统计分析
- **PgyerApi**: 应用更新服务

## 9. 代码规范和最佳实践

### 9.1 代码规范
- 遵循Kotlin官方编码规范
- 使用统一的代码格式化配置
- 强制使用类型安全的Kotlin语法

### 9.2 架构最佳实践
- 严格遵循MVP架构模式
- 合理使用Fragment生命周期
- 实现响应式编程模式
- 统一异常处理和错误上报

### 9.3 性能优化
- 启用MultiDex支持
- 使用LeakCanary检测内存泄漏
- 合理使用图片加载和缓存
- 网络请求优化和缓存策略

### 9.4 安全措施
- 代码混淆保护
- 网络传输加密
- 敏感信息本地加密存储
- 权限最小化原则

## 10. 开发工具和调试

### 10.1 调试工具
- **UETool**: UI界面调试工具
- **LeakCanary**: 内存泄漏检测
- **Debug-DB**: 数据库调试工具

### 10.2 日志系统
- 自定义日志工具 (XLog)
- 分级日志输出
- 生产环境日志控制

---

*本文档记录了iMoney项目的技术架构和开发规范，随着项目发展会持续更新维护。*