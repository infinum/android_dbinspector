package im.dino.dbinspector.domain.database.models

internal data class Database(
    val absolutePath: String,
    val path: String,
    val name: String,
    val extension: String,
    val version: String
)
