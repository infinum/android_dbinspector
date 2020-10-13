package im.dino.dbinspector.domain.shared.base

internal interface BaseRepository<InputModel, OutputModel> {

    suspend fun getPage(input: InputModel): OutputModel
}
