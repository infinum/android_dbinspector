package com.infinum.dbinspector.domain

import com.infinum.dbinspector.data.models.local.cursor.input.Order
import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.domain.database.models.Operation
import com.infinum.dbinspector.domain.shared.base.BaseConverter
import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters
import com.infinum.dbinspector.domain.shared.models.BlobPreviewMode as BlobPreviewModeModel
import com.infinum.dbinspector.domain.shared.models.Sort as SortModel
import com.infinum.dbinspector.domain.shared.models.TruncateMode as TruncateModeModel

internal interface Converters {

    interface Database {

        suspend infix fun get(parameters: DatabaseParameters.Get): Operation

        suspend infix fun import(parameters: DatabaseParameters.Import): Operation

        suspend infix fun rename(parameters: DatabaseParameters.Rename): Operation

        suspend infix fun command(parameters: DatabaseParameters.Command): Operation
    }

    interface Connection : BaseConverter<ConnectionParameters, String>

    interface Sort : BaseConverter<SortModel, Order>

    interface Schema {

        suspend infix fun getPage(parameters: ContentParameters): Query

        suspend infix fun getByName(parameters: ContentParameters): Query

        suspend infix fun dropByName(parameters: ContentParameters): Query
    }

    interface Pragma {

        suspend infix fun version(parameters: PragmaParameters.Version): Query

        suspend infix fun info(parameters: PragmaParameters.Info): Query

        suspend infix fun foreignKeys(parameters: PragmaParameters.ForeignKeys): Query

        suspend infix fun indexes(parameters: PragmaParameters.Indexes): Query
    }

    interface BlobPreview : BaseConverter<BlobPreviewModeModel, SettingsEntity.BlobPreviewMode>

    interface Truncate : BaseConverter<TruncateModeModel, SettingsEntity.TruncateMode>

    interface Settings {

        suspend infix fun get(parameters: SettingsParameters.Get): SettingsTask

        suspend infix fun linesLimit(parameters: SettingsParameters.LinesLimit): SettingsTask

        suspend infix fun linesCount(parameters: SettingsParameters.LinesCount): SettingsTask

        suspend infix fun truncateMode(parameters: SettingsParameters.Truncate): SettingsTask

        suspend infix fun blobPreviewMode(parameters: SettingsParameters.BlobPreview): SettingsTask
    }
}
