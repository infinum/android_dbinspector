# Android DbInspector

[![Build Status](https://travis-ci.org/infinum/android_dbinspector.svg?branch=master)](https://travis-ci.org/infinum/android_dbinspector)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/im.dino/dbinspector/badge.svg)](https://maven-badges.herokuapp.com/maven-central/im.dino/dbinspector)

Provides a simple way to view the contents of the in-app database for debugging purposes. No need to pull the database from a rooted phone. Also supports inspecting of the sqlite databases created by CouchBase Lite since DbInspector version 1.1.0.

![Screenshots](https://raw.github.com/infinum/android_dbinspector/master/screenshots.png)

## Usage

Add the library as a dependency to your ```build.gradle```

```groovy
debugCompile 'im.dino:dbinspector:3.3.0@aar'
```

Check the latest version on [Maven Central](http://search.maven.org/#search|ga|1|g%3A%22im.dino%22%20a%3A%22dbinspector%22).

Now you have a launcher icon for viewing you in-app database which appears only on debug builds.
You can define `@string/dbinspector_app_name` to change the launcher icon label and `@drawable/dbinspector_ic_launcher` to change the launcher icon.

You probably want to define at least `@string/dbinspector_app_name` in your `strings.xml` to avoid confusion in case of multiple apps using DbInspector on the same device.

We also maintain a [changelog](https://github.com/infinum/android_dbinspector/blob/master/CHANGELOG.md).

### Removing launcher icon

If you don't want the launcher icon to be shown, add this to your debug manifest:

```xml
<activity
    android:name="im.dino.dbinspector.activities.DbInspectorActivity">
    <intent-filter tools:node="removeAll">
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

## Support

As of version 3.0.0, DbInspector can be used with your apps on devices all the way back to Android 2.3 (API 10).
It should also work on devices with API 7-9, but that's not tested.

## Upgrading 2.x -> 3.x

Remove the `DbInspectorActivity` declaration from your app manifest, this declaration is now included in the library manifest and it gets merged in with your app manifest during the build process.

## Upgrading 1.x -> 2.x

Change the theme set to DbInspectorActivity your app manifest to an `AppCompat` theme. `DbInspectorActivity` extends `ActionBarActivity` since 2.0.0.

## Contributing

Feedback and code contributions are very much welcome. Just make a pull request with a short description of your changes. By making contributions to this project you give permission for your code to be used under the same [license](LICENSE).

## Credits

Maintained and sponsored by
[Infinum] (http://www.infinum.co).

<img src="https://infinum.co/infinum.png" width="264">
