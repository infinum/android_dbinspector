package im.dino.dbinspector.domain.database.models

internal data class Database(
    val path: String,
    val name: String,
    val extension: String,
    val version: String
) {
    val absolutePath: String
        get() = "$path/$name.$extension"
}
