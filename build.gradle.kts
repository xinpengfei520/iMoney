buildscript {
    dependencies {
        classpath("com.tencent.bugly:symtabfileuploader:latest.release")
        classpath("com.growingio.android:vds-gradle-plugin:autotrack-2.8.2")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.5.0" apply false
    id("com.android.library") version "8.5.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
    // 添加GrowingIO 无埋点 SDK 插件
    id("com.growingio.android.autotracker") version "4.3.0" apply false
}

apply("${project.rootDir}/config.gradle")
