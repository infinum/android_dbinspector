package com.infinum.dbinspector.ui.shared.views.editor

data class Token(
    val value: String,
    val type: TokenType
) {

    override fun toString(): String = value
}