package im.dino.dbinspector.domain.pragma.models

enum class TableInfoColumns {
    CID,
    NAME,
    TYPE,
    NOTNULL,
    DFLT,
    PK;

    companion object {

        operator fun invoke(index: Int) = values().single { it.ordinal == index }
    }
}