# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep ZXing classes
-keep class com.google.zxing.** { *; }
-dontwarn com.google.zxing.**

-keep class com.journeyapps.barcodescanner.** { *; }
-dontwarn com.journeyapps.barcodescanner.**

# Keep database classes
-keep class com.parking.qrmanager.DatabaseHelper** { *; }

# Keep all public classes
-keepclassmembers class * {
    public <init>(...);
}
