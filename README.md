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

Now you have a launcher icon for viewing you in-app database which appears only on debug builds.
You can override `@string/dbinspector_app_name` to change the launcher icon label and `@drawable/dbinspector_ic_launcher` to change the launcher icon.

## Support

As of version 3.0.0, DbInspector can be used with your apps on devices all the way back to Android 2.3 (API 10).
It should also work on devices with API 7-9, but that's not tested.

## Contributing

Feedback and code contributions are very much welcome. Just make a pull request with a short description of your changes. By making contributions to this project you give permission for your code to be used under the same [license](LICENSE).
