package com.infinum.dbinspector.domain.shared.base

internal interface BaseControl<Mapper : BaseMapper<*, *>, Converter> {

    val mapper: Mapper

    val converter: Converter
}
