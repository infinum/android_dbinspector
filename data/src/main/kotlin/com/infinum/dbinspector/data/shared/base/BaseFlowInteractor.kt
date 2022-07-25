package com.infinum.dbinspector.data.shared.base

public interface BaseFlowInteractor<InputModel, OutputModel> {

    public operator fun invoke(input: InputModel): OutputModel = throw NotImplementedError()
}
