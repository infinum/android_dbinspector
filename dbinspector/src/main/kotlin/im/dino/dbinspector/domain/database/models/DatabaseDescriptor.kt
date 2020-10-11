package im.dino.dbinspector.domain.database.models

internal data class DatabaseDescriptor(
    val exists: Boolean,
    val parentPath: String,
    val name: String,
    val extension: String,
    val version: String = ""
) {
    val absolutePath: String
        get() = "$parentPath/$name.$extension"
}
