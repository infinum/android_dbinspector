Changelog
=========

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
