package im.dino.dbinspector.domain.schema.models

internal enum class TriggerColumns {
    NAME,
    SQL;

    companion object {

        operator fun invoke(index: Int) = values().single { it.ordinal == index }
    }
}