# README.md

#Setup Environment 
- Android API Level 24 Compatible Project
- Kotlin 1.5.20
- gradle 6.7.1
- JVM 1.8

## Running the program
- Step 1. Sideload the app via Android Studio or APK installation
- Step 2. Run the shell script for remote config customization option
- adb shell am start -a android.intent.action.VIEW -n com.htetwill.portier.launcher/com.htetwill.portier.launcher.HomeActivity -e param v3/396825bb-b390-4ac2-8909-cd362ab0803c

## 3rd Party Library and license
Retrofit A type-safe HTTP client for Android and Java - Apache License
- handling HTTP protocols with better efficiency
- remove boilercode in Object conversion

## Technical Consideration

## Kotlin Coroutines 
- asynchronous programming while processing HTTP request/response
- handle better thread management between Main thread (UI thread) and Background thread
- smoother UI/UX across Apps

## Version 2 Performance Optimization
- improve UX by storing state/config in persistence storage of Android SharedPreference (key value pair) or SQLite Database
- improve performance in launching App by storing in-memory cache
- support privacy guard introduced by Android 10 API Level 30

## Version 2 Code Optimization
- achieve more cleaner codebase by introducing Reative design principle along with Android Jetpack Compose
- introduce Fragemnt in newer feature/screens while optimize small footprint in usage of Activity due to main App Launcher
