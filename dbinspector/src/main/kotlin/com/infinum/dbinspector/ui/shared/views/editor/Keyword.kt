package com.infinum.dbinspector.ui.shared.views.editor

internal data class Keyword(
    val value: String,
    val type: KeywordType
) {

    override fun toString(): String = value
}
