Changelog
=========

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

 * Fix: Support blob type (thanks to user ```bclymer```)

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
