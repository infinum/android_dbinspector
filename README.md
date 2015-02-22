# Android DbInspector

[![Build Status](https://travis-ci.org/infinum/android_dbinspector.svg?branch=master)](https://travis-ci.org/infinum/android_dbinspector)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/im.dino/dbinspector/badge.svg)](https://maven-badges.herokuapp.com/maven-central/im.dino/dbinspector)

Provides a simple way to view the contents of the in-app database for debugging purposes. No need to pull the database from a rooted phone. Also supports inspecting of the sqlite databases created by CouchBase Lite since DbInspector version 1.1.0.

![Screenshots](https://raw.github.com/infinum/android_dbinspector/master/screenshots.png)

## Usage

Add the library as a dependency to your ```build.gradle```

```groovy
debugCompile 'im.dino:dbinspector:(insert latest version)@aar'
```

Check the latest version on [Maven Central](http://search.maven.org/#search|ga|1|a%3A%22dbinspector%22).

Declare ```DbInspectorActivity``` in your debug manifest (`src/debug/AndroidManifest.xml`)

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <application>
        <activity
            android:name="im.dino.dbinspector.activities.DbInspectorActivity"
            android:label="Example DbInspector"
            android:theme="@style/Theme.AppCompat.Light.DarkActionBar"
            android:icon="@drawable/ic_launcher_dbinspector">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
```

Now you have a launcher icon for viewing you in-app database which appears only on debug builds.

You could also omit the ```<intent-filter />``` and open the activity programmatically by calling ```startActivity()```. Feel free to use it however it suits you as long as you comply with the [license](LICENSE).

## Support

DbInspector needs **minSdk 14** to run, but it declares **minSdk 7** so you can include it in your project which may support pre-ICS devices.

### Note

By default, when you run the app from Android Studio it may launch ```DbInspectorActivity``` instead of your launcher activity.
You can override this behaviour by editing the run configurations, at the top next to the run button and selecting your apps entry activity.

## Contributing

Feedback and code contributions are very much welcome. Just make a pull request with a short description of your changes. By making contributions to this project you give permission for your code to be used under the same [license](LICENSE).
