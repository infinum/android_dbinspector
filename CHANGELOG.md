Changelog
=========

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
