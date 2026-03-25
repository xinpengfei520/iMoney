import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    // 使用 GrowingIO 无埋点 SDK 插件
    alias(libs.plugins.autotracker)
}

// 从 local.properties 读取敏感配置
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) load(FileInputStream(file))
}

android {
    namespace = "com.xpf.p2p"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.xpf.p2p"
        minSdk = 24
        targetSdk = 34
        versionCode = getVersionCode()
        versionName = "2.0.6"
        flavorDimensions.add("versionCode")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
        resValue("string", "growingio_project_id", localProperties.getProperty("GROWINGIO_PROJECT_ID", ""))
        resValue("string", "growingio_url_scheme", localProperties.getProperty("GROWINGIO_URL_SCHEME", ""))
        // 增加 gioenable 的配置
        resValue("string", "growingio_enable", "true")
        manifestPlaceholders["JPUSH_APPKEY"] = localProperties.getProperty("JPUSH_APPKEY", "")
        manifestPlaceholders["JPUSH_PKGNAME"] = "com.xpf.p2p"
        manifestPlaceholders["JPUSH_CHANNEL"] = "developer-default"
        manifestPlaceholders["PGYER_API_KEY"] = localProperties.getProperty("PGYER_API_KEY", "")
        manifestPlaceholders["PGYER_APP_KEY"] = localProperties.getProperty("PGYER_APP_KEY", "")

        // 将密钥注入 BuildConfig，供运行时使用
        buildConfigField("String", "MOB_APP_KEY", "\"${localProperties.getProperty("MOB_APP_KEY", "")}\"")
        buildConfigField("String", "MOB_APP_SECRET", "\"${localProperties.getProperty("MOB_APP_SECRET", "")}\"")
        buildConfigField("String", "BUGLY_APP_ID", "\"${localProperties.getProperty("BUGLY_APP_ID", "")}\"")
        buildConfigField("String", "PGYER_API_KEY", "\"${localProperties.getProperty("PGYER_API_KEY", "")}\"")
        buildConfigField("String", "PGYER_APP_KEY", "\"${localProperties.getProperty("PGYER_APP_KEY", "")}\"")
        buildConfigField("String", "GROWINGIO_SERVER_HOST", "\"${localProperties.getProperty("GROWINGIO_SERVER_HOST", "")}\"")
        buildConfigField("String", "GROWINGIO_DATASOURCE_ID", "\"${localProperties.getProperty("GROWINGIO_DATASOURCE_ID", "")}\"")
        ndk {
            abiFilters.add("armeabi-v7a")
            abiFilters.add("arm64-v8a")
        }
    }

    signingConfigs {
        create("release") {
            keyAlias = localProperties.getProperty("KEYSTORE_KEY_ALIAS", "android")
            keyPassword = localProperties.getProperty("KEYSTORE_KEY_PASSWORD", "")
            storeFile = file("./android.jks")
            storePassword = localProperties.getProperty("KEYSTORE_STORE_PASSWORD", "")
        }
        getByName("debug") {
            keyAlias = localProperties.getProperty("KEYSTORE_KEY_ALIAS", "android")
            keyPassword = localProperties.getProperty("KEYSTORE_KEY_PASSWORD", "")
            storeFile = file("./android.jks")
            storePassword = localProperties.getProperty("KEYSTORE_STORE_PASSWORD", "")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    // AndroidX
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.multidex)
    // ViewModel + LiveData
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.viewpager2)
    // Network
    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.gson)
    // RxJava
    implementation(libs.rxjava2)
    implementation(libs.rxandroid2)
    // UI
    implementation(libs.banner.youth)
    implementation(libs.picasso)
    implementation(libs.countdownview)
    implementation(libs.mpandroidchart)
    // Push
    implementation(libs.jpush)
    implementation(libs.jcore)
    // Analytics
    implementation(libs.bugly.crashreport)
    implementation(platform(libs.growingio.autotracker.bom))
    implementation(libs.growingio.autotracker)
    // Utils
    implementation(libs.pgyer.update)
    implementation(libs.xxpermissions)
    // Debug
    debugImplementation(libs.leakcanary)
    debugImplementation(libs.uetool)
    debugImplementation(libs.uetool.base)
    releaseImplementation(libs.uetool.no.op)
    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
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