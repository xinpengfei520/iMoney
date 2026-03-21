# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

iMoney is an Android financial application (package: `com.xpf.p2p`) built with Kotlin + Java, using MVP architecture. It's a multi-module project with the main `app` module and a custom HTTP library module `okla-library`.

## Build Commands

```bash
# Build release APK
./gradlew assembleRelease

# Build debug APK
./gradlew assembleDebug

# Install APK to connected device
./gradlew :app:installApk

# Launch app on device
./gradlew :app:launchApp

# Run unit tests
./gradlew test

# Run Android instrumentation tests
./gradlew connectedAndroidTest

# Clean build
./gradlew clean
```

## Build Configuration

- **Gradle**: 8.7 with Kotlin DSL (`build.gradle.kts`)
- **Compile SDK**: 34 | **Min SDK**: 24 | **Target SDK**: 29
- **Java/Kotlin**: JDK 17
- **Version code**: Auto-calculated from `git rev-list HEAD --count`
- **Version catalog**: `gradle/libs.versions.toml` for plugin versions; app dependencies are declared directly in `app/build.gradle.kts`
- **Centralized config**: `config.gradle` (legacy, used by `okla-library`)

### Environment Configuration

API host is loaded from properties files at build time:
- `app/src/main/env/dev/config.properties` — development
- `app/src/main/env/test/config.properties` — testing
- `app/src/main/env/pro/config.properties` — production

Set via `buildConfigField("String", "HOST", getHOST("pro"))` in build types.

### Build Types

- **Release**: ProGuard enabled, resource shrinking, production HOST
- **Debug**: No minification, debug logging enabled (`ENABLE_DEBUG = true`)

## Architecture

### MVP Pattern

Feature modules under `app/src/main/java/com/xpf/p2p/ui/` follow MVP with a Contract pattern:

```
ui/<feature>/
├── contract/    # IModel, IView, IPresenter interfaces
├── model/       # Data layer implementation
├── presenter/   # Business logic
├── view/        # Activity/Fragment UI
└── listener/    # Custom listeners (optional)
```

Base classes in `com.xpf.p2p.base/`:
- `BaseActivity` — Activity foundation with ActivityManager integration
- `MvpBaseActivity<V, T>` — Generic MVP Activity with Presenter lifecycle
- `BaseFragment` — Fragment base with LoadingPage support
- `MvpBasePresenter<V>` — Generic Presenter base

### Module Structure

- **app** (`com.xpf.p2p`) — Main application: activities, fragments, adapters, entities, utils, widgets
- **okla-library** (`com.xpf.http`) — Custom HTTP client wrapping OkHttp, with Aliyun HTTPDNS support

### Navigation

Main UI uses `RadioGroup + Fragment + ViewPager` pattern for tab-based navigation.

## Key Dependencies

| Category | Libraries |
|----------|-----------|
| Networking | OkHttp 4.x, Retrofit 2.9, Gson, custom okla-library |
| UI | Material Design, Banner, MagicIndicator, MPAndroidChart |
| Reactive | RxJava 2, RxAndroid 2 |
| Image | Picasso, Glide |
| Analytics | Bugly (crash), GrowingIO (behavior), UMeng |
| Push | JPush (Jiguang) |
| Storage | MMKV, Room |
| Debug | LeakCanary, UETool (debug builds only) |
| Permissions | XXPermissions |

## CI/CD

GitHub Actions workflow (`.github/workflows/android.yml`): triggers on tag push (`v*`), builds release APK with JDK 17, and uploads to GitHub Releases.

## Multi-language Support

Resources in `values/` (Chinese default), `values-en/`, `values-zh-rTW/`. Language switching via `LocaleUtils`.

## NDK / ABI

Only `armeabi-v7a` and `arm64-v8a` ABIs are included. JNI libs in `app/src/main/jniLibs/`.
