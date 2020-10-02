package im.dino.dbinspector.domain.pragma.schema.models

internal enum class IndexListColumns {
    SEQ,
    NAME,
    UNIQUE;

    companion object {

        operator fun invoke(index: Int) = values().single { it.ordinal == index }
    }
}
