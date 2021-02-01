package com.infinum.dbinspector.ui.shared.base.lifecycle

internal data class LifecycleConnection(
    val databaseName: String? = null,
    val databasePath: String? = null,
    val schemaName: String? = null
) {
    val hasDatabaseData = databaseName.isNullOrBlank().not() && databasePath.isNullOrBlank().not()

    val hasSchemaData = hasDatabaseData && schemaName.isNullOrBlank().not()
}
