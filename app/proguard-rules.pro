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
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

#inhouse rules
-keep class com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models.** { *;}
-flattenpackagehierarchy

-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken



-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Add *one* of the following rules to your Proguard configuration file.
# Alternatively, you can annotate classes and class members with @androidx.annotation.Keep

# keep everything in this package from being removed or renamed
-keep class com.allformats.video.player.downloader.video_player.Fragment.** { *; }

# keep everything in this package from being renamed only
-keepnames class com.allformats.video.player.downloader.video_player.Fragment.** { *; }

-keep class com.allformats.video.player.downloader.view.** { *; }

# keep everything in this package from being renamed only
-keepnames class com.allformats.video.player.downloader.view.** { *; }



-keep class com.allformats.video.player.downloader.ds_tube_android_util.** { *; }

# keep everything in this package from being renamed only
-keepnames class com.allformats.video.player.downloader.ds_tube_android_util.** { *; }



-keep class com.allformats.video.player.downloader.ds_tube_connect.** { *; }

# keep everything in this package from being renamed only
-keepnames class com.allformats.video.player.downloader.ds_tube_connect.** { *; }


-keep class com.allformats.video.player.downloader.data.** { *; }

# keep everything in this package from being renamed only
-keepnames class com.allformats.video.player.downloader.data.** { *; }