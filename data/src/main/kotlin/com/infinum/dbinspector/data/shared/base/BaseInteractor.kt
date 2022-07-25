package com.infinum.dbinspector.data.shared.base

public interface BaseInteractor<InputModel, OutputModel> {

    public suspend operator fun invoke(input: InputModel): OutputModel = throw NotImplementedError()
}
