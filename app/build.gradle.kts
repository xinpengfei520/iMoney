import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // 使用 GrowingIO 无埋点 SDK 插件
    id("com.growingio.android.autotracker")
}

//apply plugin: "bugly"
//
//bugly {
//    // 注册时分配的 App ID
//    appId = "c84e7e9ad7"
//    // 注册时分配的 App Key
//    appKey = "17ec4880-a08f-4bcd-9e69-46b96e639047"
//}

android {
    namespace = "com.xpf.p2p"
    useLibrary("org.apache.http.legacy")
    compileSdk = 34
    defaultConfig {
        applicationId = "com.xpf.p2p"
        minSdk = 24
        targetSdk = 29
        versionCode = getVersionCode()
        versionName = "1.4.13"
        flavorDimensions("versionCode")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
        resValue("string", "growingio_project_id", "9addc2e4f77786ba")
        resValue("string", "growingio_url_scheme", "growing.2724aa529418df26")
        // 增加 gioenable 的配置
        resValue("string", "growingio_enable", "true")
        manifestPlaceholders["JPUSH_APPKEY"] = "069229aa6eb2056acb0fa7e1"
        manifestPlaceholders["JPUSH_PKGNAME"] = "com.xpf.p2p"
        manifestPlaceholders["JPUSH_CHANNEL"] = "developer-default"
        ndk {
            abiFilters.add("armeabi-v7a")
            abiFilters.add("arm64-v8a")
        }
    }

    signingConfigs {
        create("release") {
            keyAlias = "android"
            keyPassword = "android"
            storeFile = file("./android.jks")
            storePassword = "android"
        }
        getByName("debug") {
            keyAlias = "android"
            keyPassword = "android"
            storeFile = file("./android.jks")
            storePassword = "android"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isZipAlignEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
            // API HOST(生产环境：pro 研发环境：dev 测试环境：test)
            buildConfigField("String", "HOST", getHOST("pro"))
            // Log日志开关，false不显示log，true显示log
            buildConfigField("boolean", "ENABLE_DEBUG", "false")
            android.applicationVariants.all {
                outputs.all {
                    if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                        outputFileName = getApkName()
                    }
                }
            }
        }

        debug {
            isMinifyEnabled = false
            // API HOST(生产环境：pro 研发环境：dev 测试环境：test)
            buildConfigField("String", "HOST", getHOST("pro"))
            // Log日志开关，false不显示log，true显示log
            buildConfigField("boolean", "ENABLE_DEBUG", "true")
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    viewBinding { enable = true }

    lint {
        abortOnError = false
        disable.add("ProtectedPermissions")
        disable.add("GoogleAppIndexingWarning")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.?ar"))))
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    testImplementation("junit:junit:4.13.2")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.youth.banner:banner:1.4.10")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("com.github.iwgang:countdownview:2.1.6")
    implementation("com.tencent.bugly:crashreport:latest.release")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.xsir:PgyerAndroidAppUpdate:1.0.0")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("me.jessyan:autosize:1.2.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.11")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("com.github.getActivity:XXPermissions:18.63")
    implementation("cn.jiguang.sdk:jpush:3.3.6")
    implementation("cn.jiguang.sdk:jcore:2.1.6")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("com.github.hackware1993:MagicIndicator:1.7.0")
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.12")
    debugImplementation("com.github.eleme.UETool:uetool:1.3.4")
    debugImplementation("com.github.eleme.UETool:uetool-base:1.3.4")
    releaseImplementation("com.github.eleme.UETool:uetool-no-op:1.3.4")
    // if you want to show more attrs about Fresco"s DraweeView
    debugImplementation("com.github.eleme.UETool:uetool-fresco:1.3.4")
    // Import the BoM for the GrowingIO platform
    implementation(platform("com.growingio.android:autotracker-bom:4.3.0"))
    //GrowingIO 无埋点 SDK
    implementation("com.growingio.android:autotracker")
    implementation(project(":okla-library"))
}

fun getApkFullPath(): String {
    return rootDir.absolutePath + "/app/build/outputs/apk/release/" + getApkName()
}

fun getApkName(): String {
    return "iMoney_android_V${android.defaultConfig.versionName}_${android.defaultConfig.versionCode}_${releaseTime()}.apk"
}

fun releaseTime(): String {
    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
}

fun getUpdateDescription(): String {
    return "1.GrowingIO;\n2.fixed bugs;\n3.发布V1.4.5;"
}

//tasks.register("uploadApk") {
//    doLast {
//        val command =
//            "curl -F \"file=@${getApkFullPath()}\" -F \"uKey=${readProperties("pgyer.userKey")}\" -F \"_api_key=${
//                readProperties("pgyer.apiKey")
//            }\" -F \"buildUpdateDescription=${getUpdateDescription()}\" https://www.pgyer.com/apiv2/app/upload"
//        executeCommand(command)
//    }
//}
//
//uploadApk.dependsOn("assembleRelease")

tasks.register("installApk") {
    doLast {
        val command = "adb install -r ${getApkFullPath()}"
        executeCommand(command)
    }
}

tasks.register("launchApp") {
    doLast {
        val command = "adb shell am start ${android.defaultConfig.applicationId}/.MainActivity"
        executeCommand(command)
    }
}

// 获取API HOST
@SuppressWarnings("GroovyVariableNotAssigned")
fun getHOST(env: String): String {
    var host = ""
    val props = Properties()
    val propFile = file("src/main/env/$env/config.properties")
    if (propFile.canRead()) {
        props.load(FileInputStream(propFile))
        host = props["HOST"] as String
    }
    return host
}

fun executeCommand(command: String) {
    println("command:$command")
    try {
        exec {
            commandLine("bash", "-c", command)
        }
        println("command execute success~")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun getVersionCode(): Int {
    val cmd = "git rev-list HEAD --count"
    val process = Runtime.getRuntime().exec(cmd, arrayOf(), rootDir)
    val inputStreamReader = BufferedReader(InputStreamReader(process.inputStream))
    val gitVersion = inputStreamReader.readText()
    println("gitVersion: $gitVersion")
    process.inputStream.close()
    inputStreamReader.close()
    return gitVersion.trim().toInt()
}

/*
 * 打包命令：
 * ./gradlew assembleRelease
 * 打包上传 APK:
 * ./gradlew :app:uploadApk
 * 安装 APK:
 * ./gradlew :app:installApk
 * 启动 APK:
 * ./gradlew :app:launchApp
 */