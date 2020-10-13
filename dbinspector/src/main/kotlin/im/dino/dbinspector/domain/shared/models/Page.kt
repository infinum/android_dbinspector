package im.dino.dbinspector.domain.shared.models

internal data class Page(
    val nextPage: Int? = null,
    val fields: List<String>
)
