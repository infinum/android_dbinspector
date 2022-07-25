package com.infinum.dbinspector.domain.shared.base

internal interface BaseControl<Mapper : BaseMapper<*, *>, Converter : BaseConverter<*, *>> {

    val mapper: Mapper

    val converter: Converter
}
