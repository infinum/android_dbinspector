# Android DbView

Provides a simple way to view the contents of the in-app database for debugging purposes. No need to pull the database from a rooted phone.

![Screenshots](https://raw.github.com/reisub/dbview/master/screenshots.png)

## Usage

1. Add the library as a dependency to your project (maven .aar coming soon)

```groovy
debugCompile 'im.dino:DbView:1.0.1@aar'
```

2. Declare the DbViewActivity in your debug manifest (`src/debug/AndroidManifest.xml`)

```xml
<activity
    android:name="im.dino.dbview.activities.DbViewActivity"
    android:label="Example DbView"
    android:icon="@drawable/ic_launcher_dbview">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />

        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

Now you have a launcher icon for viewing you in-app database which shows only on debug builds.

### Note

By default, Android Studio will launch ```DbViewActivity``` when you run the app.
You can override this behaviour by editing the run configurations, at the top next to the run button and selecting your apps entry activity.
