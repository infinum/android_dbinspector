package im.dino.dbinspector.domain.shared.base

internal interface BaseUseCase<InputModel, OutputModel> {

    suspend operator fun invoke(input: InputModel): OutputModel
}
