DbView
======

A small android library which enables you to take a look inside you in-app database.

## Usage

1. Add the library as a dependency to your project (maven .aar coming soon)
2. Declare the activity in your manifest:

```xml
<activity
    android:name="im.dino.dbview.activities.DbViewActivity"
    android:label="My App DbView" >
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />

        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

Now you have a launcher icon for viewing you in-app database.

NOTE:
Don't forget to remove the activity declaration from the manifest for production releases!
