# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/eva/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
-dontusemixedcaseclassnames
-ignorewarnings
-verbose

#Keep native methods
-keepclassmembers class * {
    native <methods>;
}

#data class
-keep class com.chsapps.yt_hongjinyoung.api.model.** { *; }
-keep class com.chsapps.yt_hongjinyoung.data.**{ *; }
-keep class com.chsapps.yt_hongjinyoung.event.data.**{ *; }

#webview
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

#image upload
-keepclassmembers class * {
    public void openFileChooser(android.webkit.ValueCallback, java.lang.String);
    public void openFileChooser(android.webkit.ValueCallback);
    public void openFileChooser(android.webkit.ValueCallback, java.lang.String, java.lang.String);
    public boolean onShowFileChooser(android.webkit.WebView, android.webkit.ValueCallback, android.webkit.WebChromeClient.FileChooserParams);
}

#WebChromeClient
-keepclassmembers class * extends android.webkit.WebChromeClient { *; }

# search view
-keep class android.support.v7.widget.SearchView { *; }

#httpclient
-dontnote android.net.http.*
-dontnote org.apache.http.**

-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}

-keep class **.R$*