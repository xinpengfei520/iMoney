buildscript {
    dependencies {
        classpath("com.tencent.bugly:symtabfileuploader:latest.release")
        //classpath("com.growingio.android:vds-gradle-plugin:autotrack-2.8.2")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    // 添加GrowingIO 无埋点 SDK 插件
    alias(libs.plugins.autotracker) apply false
}

apply("${project.rootDir}/config.gradle")
