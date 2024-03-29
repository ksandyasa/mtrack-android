# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\AndroidDevelopmentTools\android-sdk-windows\sdk/tools/proguard/proguard-android.txt
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
-dontwarn com.android.support.**
-dontwarn de.hdodenhof.**
-dontwarn org.codehaus.mojo.animal_sniffer.**
-dontwarn com.squareup.**
-dontwarn okio.**
-dontwarn com.wdullaer.**
-dontwarn uk.co.chrisjenx.**
-dontwarn org.apache.lang.**
-dontwarn com.borax12.materialdaterangepicker.**
-dontwarn com.google.maps.android.**
-dontwarn com.google.android.gms.**
