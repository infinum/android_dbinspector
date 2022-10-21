Changelog
=========

## Version 5.4.9

_2022-10-21_

* Update Kotlin to 1.7.20.
* Update dependencies to stable version.
* Implement Android 13 compatibility and changes.
* Refactor logger interface and classes.
* Add KDoc on all public classes.

## Version 5.4.8

_2022-08-05_

* Update Kotlin to 1.7.10.
* Update dependencies to stable version.

## Version 5.4.7

_2022-06-10_

* Update Kotlin to 1.7.0.
* Update dependencies to stable version.
* Implement dynamic shortcut.

## Version 5.4.6

_2022-05-11_

* Update dependencies to stable version.
* Fix some tests.
* Fix Gradle scripts.

## Version 5.4.5

_2022-04-09_

* Update dependencies to stable version.

## Version 5.4.4

_2022-03-26_

* Compile with SDK 32.
* Update dependencies to stable version.
* Switch to Material3 theme.
* Replace FuzzySearch library with Levenshtein algorithm implementation.

## Version 5.4.3

_2021-12-29_

* Fix implicit Kotlin 1.6 changes on static show methods.

## Version 5.4.2

_2021-12-24_

* Update to Kotlin 1.6.10.
* Update Coroutines to 1.6.0.
* Update Gradle wrapper to 7.3.3. 
* Refactor edit database activity to rename database dialog.

## Version 5.4.1

_2021-09-25_

* Update to Kotlin 1.5.31.
* Fix backwards compatibility for specific Android API levels.

## Version 5.4.0

_2021-09-13_

* Update to Kotlin 1.5.30.
* Add blur for Android 12 devices.
* Replace all AlertDialogs with BottomSheetDialogFragments.

## Version 5.3.9

_2021-08-23_

* Fix R8 collisions on obfuscated class names.

## Version 5.3.8

_2021-08-21_

* Update dependencies to stable version.

## Version 5.3.7

_2021-07-22_

* Update Kotlin to 1.5.21.
* Remove lambdas as method parameters.
* Fix memory leaks.

## Version 5.3.6

_2021-07-09_

* Update Kotlin to 1.5.20.
* Update AndroidX Datastore to RC01.

## Version 5.3.5

_2021-05-24_

* Update Kotlin to 1.5.10.

## Version 5.3.4

_2021-05-20_

* Update dependencies.
* Add unit tests.
* Fix small UI bugs.

## Version 5.3.3

_2021-04-30_

* Fix refreshing schema or content when editor has affected rows.

## Version 5.3.2

_2021-04-26_

* Fix crash on editor history.

## Version 5.3.1

_2021-04-25_

* Remove Timber dependency.
* Implement an optional logger.
* Update dependencies to more stable releases.

## Version 5.3.0

_2021-04-10_

* Update Gradle from 6.8.3 to 7.0.
* Migrate from dependencies.gradle to version catalogs.
* Update dependencies to more stable releases.
* Remove Bintray deployment configuration.

## Version 5.2.9

_2021-03-25_

* Update dependencies to more stable releases.
* Fix Lint issues.

## Version 5.2.8

_2021-03-11_

* Add tablet support on databases list.
* Update dependencies to more stable releases.
* Fix Lint issues.
* Enable Proguard for release builds.

## Version 5.2.7

_2021-02-24_

* Implement history in editor.
* Fix word tokenization in editor.

## Version 5.2.6

_2021-02-18_

* Expose public String resource launcher name.

## Version 5.2.5

_2021-02-17_

* Fix MaterialToolbar subtitle descender cutoff.

## Version 5.2.4

_2021-02-11_

* Update more dependencies to stable versions.

## Version 5.2.3

_2021-02-03_

* Fix missing launcher assets

## Version 5.2.2

_2021-02-01_

* Fix Timber planting multiple trees.
* Update Gradle plugin to 4.1.2.
* Update dependencies.
* Add CPD plugin.
* Add ktlint plugin.

## Version 5.2.1

_2021-01-22_

* Fix Timber planting multiple trees.
* Update Gradle plugin to 4.1.2.
* Update dependencies.

## Version 5.2.0

_2021-01-11_

* Add an editor and ability to execute custom SQL queries
* Implement adaptive icon support
* Change launcher alias behaviour

## Version 5.1.0

_2020-12-31_

* Implement persistent settings
* Add blob preview
* Add text content preview
* Refactor and optimise architecture

## Version 5.0.1

_2020-12-23_

* Fix touch event collision between SwipeRefreshLayout and HorizontalScrollView
* Update dependencies to latest stable and RC versions

## Version 5.0.0

_2020-11-01_

For breaking changes check [wiki migration pages](https://github.com/infinum/android_dbinspector/wiki/Migrations).

* minSdk is now 21
* Sample now provides 3 test databases
* Refactor codebase and migrate to Kotlin
* Replace manual pagination with automatic paging
* Add a no op package
* Redesign all screens
* Streamline deployment
* Implement sorting tables per columns

## Version 4.0.0

_2019-10-26_

* Fix: #57
* Fix: #55 (thanks @orionlee)
* Migrated project and example app to androidx
* minSdk is now 19
* The example app is now all Kotlin and uses Room

## Version 3.4.1

_2017-02-08_

* Fix: Sharing content provider authority is now per-app specific and prefixed with dbinspector (thanks @domagojkorman)

## Version 3.4.0

_2016-12-23_

* Fix: Add permission handling, fixes #42 (thanks @domagojkorman)
* Fix: Fix exception while while sharing db, fixes #43 (thanks @domagojkorman)
* New: Make dbinspector launcher icon have same name as app if not overridden

## Version 3.3.0

_2016-06-27_

* New: Action to delete contents of a table (thanks @ignaciotcrespo)
* Fix: Build error when using support lib 24.0.0 (thanks @rkrsgithub and @gmbett)

## Version 3.2.4

_2016-05-04_

* Fix: The new CBL format is now properly supported (.cblite2 directory)

## Version 3.2.3

_2016-03-04_

* Fix: Using in app which uses support libs 23.2.0+ (thanks @MarcBernstein)

## Version 3.2.2

_2016-01-27_

* New: Support new CBL file suffix (.cblite2)

## Version 3.2.1

_2015-07-12_

* Fix: Resolved crash that happened if you were on the last page of the content and switched the display to show more rows per page

## Version 3.2.0

_2015-07-09_

* New: Show external databases if in folder returned by `context.getExternalFilesDir(null)` (thanks to @Aexyn)
* New: Add option to share selected database (thanks to @dmarin)
* New: Import database from file (thanks to @dmarin)
* Fix: Set taskAffinity for `DbInspectorActivity` so the launcher opens DbInspector and not the app (thanks to @nicopico-dev)

## Version 3.1.0

_2015-03-25_

 * Fix: [#14 Database chooser doesn't show db with name without extension](/../../issues/14)
 * New: New table views - `Foreign keys` and `Indexes` (thanks to @zplesac)

## Version 3.0.1

_2015-03-08_

 * Fix: Added a way to set DbInspector launcher label from app via `@string/dbinspector_app_name`

## Version 3.0.0

_2015-03-07_

 * New: Support for older devices (thanks to @PrashamTrivedi)
 * New: Search on the table list (thanks to @PrashamTrivedi)
 * New: The database can now be copied to the app folder for easy access with `adb pull` (thanks to @PrashamTrivedi)
 * New: No need to add the DbInspectorActivity declaration to the app manifest anymore (actually your app won't build if you do add it), this declaration is now in the library manifest - this change is the reason for the major version bump

## Version 2.0.0

_2015-01-11_

 * Fix: Closing both cursor and database after query to prevent leaking the connection
 * Fix: DbInspector now uses ActionBarActivity so you can (and **need** to) use an AppCompat theme - this change is the reason for the major version bump

## Version 1.1.0

_2014-10-17_

 * New: Support for inspecting sqlite database created internally by CouchBase Lite
 * Fix: Show table content by default and don't lose state on recreate
 * Fix: Close cursor after database query to prevent leaking the connection

## Version 1.0.6

_2014-04-07_

 * Fix: Don't truncate structure table to _rows per page_ number of rows

## Version 1.0.5

_2014-03-31_

 * Fix: Support blob type (thanks to @bclymer)

## Version 1.0.4

_2014-03-27_

 * Fix: Prefixed almost all of the resources with _dbinspector__ to avoid overriding the id in the parent project

## Version 1.0.3

_2014-03-27_

 * Fix: Prefixed view identifiers with _dbinspector__ to avoid overriding the id in the parent project

## Version 1.0.2

_2014-03-26_

 Renamed project and maven artifact.
 
## Version 1.0.1

_2014-03-15_

 * New: Added launcher icon


## Version 1.0.0

_2014-03-15_

Initial release.
