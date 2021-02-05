# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.chinamobile.ysx.** {*;}
-keep class org.conscrypt.** {*;}
-keep class org.apache.commons.** {*;}
-keep class javax.annotation.** {*;}
-dontwarn com.chinamobile.**
-keep class us.zoom.** {*;}
-keep class us.zoom.**
-dontwarn us.zoom.**
-keep class com.zipow.** {*;}
-keep class com.zipow.**
-keep class org.webrtc.** {*;}
-keep class org.webrtc.**
-dontwarn com.zipow.**
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }
-keep class javax.ws.rs.** {*;}
-keep class javax.awt.** {*;}
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn org.apache.http.**
