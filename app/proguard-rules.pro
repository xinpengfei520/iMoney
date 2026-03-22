# iMoney ProGuard Rules

#---------------------------------基本指令区----------------------------------
-optimizationpasses 5
-dontskipnonpubliclibraryclassmembers
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses,Signature,SourceFile,LineNumberTable

#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends androidx.multidex.MultiDexApplication
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View

-keep public class * extends android.view.View {
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep public class * implements java.io.Serializable { *; }

-keep class **.R$* { *; }

-keepclassmembers class * {
    void *(*Event);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keepclasseswithmembernames class * {
    native <methods>;
}

#---------------------------------WebView------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
    public void *(android.webkit.WebView, java.lang.String);
}

#---------------------------------实体类---------------------------------
-keep class com.xpf.p2p.entity.** { *; }

#---------------------------------第三方库-------------------------------

# 支付宝
-keep class com.alipay.sdk.app.PayTask { public *; }
-keep class com.alipay.sdk.app.AuthTask { public *; }
-keep public class * extends android.os.IInterface
-dontwarn com.alipay.**
-keep class com.alipay.** { *; }

# 微信支付
-keep class com.tencent.mm.sdk.** { *; }
-dontwarn com.tencent.mm.**

# OkHttp + OkIo
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
-dontwarn okio.**
-keep class okio.** { *; }

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Exceptions

# Gson
-keep class com.google.gson.** { *; }
-keep class sun.misc.Unsafe { *; }

# Picasso
-dontwarn com.squareup.picasso.**
-keep class com.squareup.picasso.** { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# Bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.** { *; }

# JPush
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }
-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

# MobSDK / ShareSDK
-keep class com.mob.** { *; }
-keep class cn.smssdk.** { *; }
-dontwarn com.mob.**
-dontwarn cn.sharesdk.**
-keep class cn.sharesdk.** { *; }

# UMeng
-dontwarn com.umeng.**
-keep class com.umeng.** { *; }

# Banner
-keep class com.youth.banner.** { *; }

# GrowingIO
-keep class com.growingio.** { *; }
-dontwarn com.growingio.**
-keepnames class * extends android.view.View
-keepnames class * extends androidx.fragment.app.Fragment
-keep class androidx.viewpager.widget.ViewPager { *; }
-keep class androidx.viewpager.widget.ViewPager$** { *; }

# XXPermissions
-keep class com.hjq.permissions.** { *; }

# OkHttpUtils (okla-library)
-dontwarn com.zhy.http.**
-keep class com.zhy.http.** { *; }

# FastJson
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }

# MPAndroidChart
-keep class com.github.mikephil.charting.** { *; }

# ZXing
-keep class com.google.zxing.** { *; }
-dontwarn com.google.zxing.**

# Apache HTTP (legacy)
-keep class org.apache.http.** { *; }
-dontwarn org.apache.**
