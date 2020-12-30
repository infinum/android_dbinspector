package com.infinum.dbinspector.domain.shared.base

internal interface BaseMapper<Input, Output> {

    fun mapLocalToDomain(model: Input): Output = throw NotImplementedError()

    fun mapDomainToLocal(model: Output): Input = throw NotImplementedError()
}
