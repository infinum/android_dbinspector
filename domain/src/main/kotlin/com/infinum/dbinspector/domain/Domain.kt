package com.infinum.dbinspector.domain

import com.infinum.dbinspector.data.Data
import com.infinum.dbinspector.domain.connection.ConnectionRepository
import com.infinum.dbinspector.domain.connection.control.ConnectionControl
import com.infinum.dbinspector.domain.connection.control.converters.ConnectionConverter
import com.infinum.dbinspector.domain.connection.control.mappers.ConnectionMapper
import com.infinum.dbinspector.domain.connection.usecases.CloseConnectionUseCase
import com.infinum.dbinspector.domain.connection.usecases.OpenConnectionUseCase
import com.infinum.dbinspector.domain.database.DatabaseRepository
import com.infinum.dbinspector.domain.database.control.DatabaseControl
import com.infinum.dbinspector.domain.database.control.converters.DatabaseConverter
import com.infinum.dbinspector.domain.database.control.mappers.DatabaseMapper
import com.infinum.dbinspector.domain.database.usecases.CopyDatabaseUseCase
import com.infinum.dbinspector.domain.database.usecases.GetDatabasesUseCase
import com.infinum.dbinspector.domain.database.usecases.ImportDatabasesUseCase
import com.infinum.dbinspector.domain.database.usecases.RemoveDatabaseUseCase
import com.infinum.dbinspector.domain.database.usecases.RenameDatabaseUseCase
import com.infinum.dbinspector.domain.history.HistoryRepository
import com.infinum.dbinspector.domain.history.control.HistoryControl
import com.infinum.dbinspector.domain.history.control.converters.HistoryConverter
import com.infinum.dbinspector.domain.history.control.mappers.ExecutionMapper
import com.infinum.dbinspector.domain.history.control.mappers.HistoryMapper
import com.infinum.dbinspector.domain.history.usecases.ClearHistoryUseCase
import com.infinum.dbinspector.domain.history.usecases.GetHistoryUseCase
import com.infinum.dbinspector.domain.history.usecases.GetSimilarExecutionUseCase
import com.infinum.dbinspector.domain.history.usecases.RemoveExecutionUseCase
import com.infinum.dbinspector.domain.history.usecases.SaveExecutionUseCase
import com.infinum.dbinspector.domain.pragma.PragmaRepository
import com.infinum.dbinspector.domain.pragma.control.PragmaControl
import com.infinum.dbinspector.domain.pragma.control.converters.PragmaConverter
import com.infinum.dbinspector.domain.pragma.control.mappers.PragmaMapper
import com.infinum.dbinspector.domain.pragma.usecases.GetForeignKeysUseCase
import com.infinum.dbinspector.domain.pragma.usecases.GetIndexesUseCase
import com.infinum.dbinspector.domain.pragma.usecases.GetTableInfoUseCase
import com.infinum.dbinspector.domain.pragma.usecases.GetTablePragmaUseCase
import com.infinum.dbinspector.domain.pragma.usecases.GetTriggerInfoUseCase
import com.infinum.dbinspector.domain.raw.RawRepository
import com.infinum.dbinspector.domain.raw.usecases.GetAffectedRowsUseCase
import com.infinum.dbinspector.domain.raw.usecases.GetRawQueryHeadersUseCase
import com.infinum.dbinspector.domain.raw.usecases.GetRawQueryUseCase
import com.infinum.dbinspector.domain.schema.table.TableRepository
import com.infinum.dbinspector.domain.schema.table.usecases.DropTableContentUseCase
import com.infinum.dbinspector.domain.schema.table.usecases.GetTableUseCase
import com.infinum.dbinspector.domain.schema.table.usecases.GetTablesUseCase
import com.infinum.dbinspector.domain.schema.trigger.TriggerRepository
import com.infinum.dbinspector.domain.schema.trigger.usecases.DropTriggerUseCase
import com.infinum.dbinspector.domain.schema.trigger.usecases.GetTriggerUseCase
import com.infinum.dbinspector.domain.schema.trigger.usecases.GetTriggersUseCase
import com.infinum.dbinspector.domain.schema.view.ViewRepository
import com.infinum.dbinspector.domain.schema.view.usecases.DropViewUseCase
import com.infinum.dbinspector.domain.schema.view.usecases.GetViewUseCase
import com.infinum.dbinspector.domain.schema.view.usecases.GetViewsUseCase
import com.infinum.dbinspector.domain.settings.SettingsRepository
import com.infinum.dbinspector.domain.settings.control.SettingsControl
import com.infinum.dbinspector.domain.settings.control.converters.SettingsConverter
import com.infinum.dbinspector.domain.settings.control.mappers.SettingsMapper
import com.infinum.dbinspector.domain.settings.usecases.GetSettingsUseCase
import com.infinum.dbinspector.domain.settings.usecases.RemoveIgnoredTableNameUseCase
import com.infinum.dbinspector.domain.settings.usecases.SaveBlobPreviewModeUseCase
import com.infinum.dbinspector.domain.settings.usecases.SaveIgnoredTableNameUseCase
import com.infinum.dbinspector.domain.settings.usecases.SaveLinesCountUseCase
import com.infinum.dbinspector.domain.settings.usecases.SaveTruncateModeUseCase
import com.infinum.dbinspector.domain.settings.usecases.ToggleLinesLimitUseCase
import com.infinum.dbinspector.domain.shared.control.ContentControl
import com.infinum.dbinspector.domain.shared.converters.BlobPreviewConverter
import com.infinum.dbinspector.domain.shared.converters.ContentConverter
import com.infinum.dbinspector.domain.shared.converters.SortConverter
import com.infinum.dbinspector.domain.shared.converters.TruncateModeConverter
import com.infinum.dbinspector.domain.shared.mappers.BlobPreviewModeMapper
import com.infinum.dbinspector.domain.shared.mappers.CellMapper
import com.infinum.dbinspector.domain.shared.mappers.ContentMapper
import com.infinum.dbinspector.domain.shared.mappers.TruncateModeMapper
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module

public object Domain {

    public object Constants {

        public object Limits {
            public const val PAGE_SIZE: Int = 100
            public const val INITIAL_PAGE: Int = 1
        }

        public object Settings {
            public const val LINES_LIMIT_MAXIMUM: Int = 100
            public const val BLOB_DATA_PLACEHOLDER: String = "[ DATA ]"
        }
    }

    public object Qualifiers {
        public val TABLES: StringQualifier = StringQualifier("domain.qualifiers.tables")
        public val VIEWS: StringQualifier = StringQualifier("domain.qualifiers.views")
        public val TRIGGERS: StringQualifier = StringQualifier("domain.qualifiers.triggers")
    }

    public fun modules(): List<Module> =
        Data.modules().plus(
            listOf(
                database(),
                connection(),
                settings(),
                schema(),
                pragma(),
                rawQuery(),
                history(),
                shared()
            )
        )

    private fun database() = module {
        factory<Mappers.Database> { DatabaseMapper() }
        factory<Converters.Database> { DatabaseConverter() }
        factory<Control.Database> { DatabaseControl(get(), get()) }

        factory<Repositories.Database> {
            DatabaseRepository(get(), get(), get(), get(), get(), get())
        }

        factory<UseCases.GetDatabases> { GetDatabasesUseCase(get(), get(), get()) }
        factory<UseCases.ImportDatabases> { ImportDatabasesUseCase(get()) }
        factory<UseCases.RemoveDatabase> { RemoveDatabaseUseCase(get()) }
        factory<UseCases.CopyDatabase> { CopyDatabaseUseCase(get()) }
        factory<UseCases.RenameDatabase> { RenameDatabaseUseCase(get()) }
    }

    private fun connection() = module {
        single<Mappers.Connection> { ConnectionMapper() }
        single<Converters.Connection> { ConnectionConverter() }
        single<Control.Connection> { ConnectionControl(get(), get()) }

        single<Repositories.Connection> { ConnectionRepository(get(), get(), get()) }

        factory<UseCases.OpenConnection> { OpenConnectionUseCase(get()) }
        factory<UseCases.CloseConnection> { CloseConnectionUseCase(get()) }
    }

    private fun settings() = module {
        factory<Mappers.Settings> { SettingsMapper(get(), get()) }
        factory<Converters.Settings> { SettingsConverter(get(), get()) }
        factory<Control.Settings> { SettingsControl(get(), get()) }

        factory<Repositories.Settings> {
            SettingsRepository(get(), get(), get(), get(), get(), get(), get(), get())
        }

        factory<UseCases.GetSettings> { GetSettingsUseCase(get()) }
        factory<UseCases.SaveIgnoredTableName> { SaveIgnoredTableNameUseCase(get()) }
        factory<UseCases.RemoveIgnoredTableName> { RemoveIgnoredTableNameUseCase(get()) }
        factory<UseCases.SaveLinesCount> { SaveLinesCountUseCase(get()) }
        factory<UseCases.ToggleLinesLimit> { ToggleLinesLimitUseCase(get()) }
        factory<UseCases.SaveTruncateMode> { SaveTruncateModeUseCase(get()) }
        factory<UseCases.SaveBlobPreviewMode> { SaveBlobPreviewModeUseCase(get()) }
    }

    private fun schema() = module {
        factory<Repositories.Schema>(qualifier = Qualifiers.TABLES) {
            TableRepository(get(), get(), get(), get())
        }
        factory<Repositories.Schema>(qualifier = Qualifiers.VIEWS) {
            ViewRepository(get(), get(), get(), get())
        }
        factory<Repositories.Schema>(qualifier = Qualifiers.TRIGGERS) {
            TriggerRepository(get(), get(), get(), get())
        }

        factory<UseCases.GetTables> { GetTablesUseCase(get(), get(qualifier = Qualifiers.TABLES)) }
        factory<UseCases.GetViews> { GetViewsUseCase(get(), get(qualifier = Qualifiers.VIEWS)) }
        factory<UseCases.GetTriggers> { GetTriggersUseCase(get(), get(qualifier = Qualifiers.TRIGGERS)) }
        factory<UseCases.GetTableInfo> { GetTableInfoUseCase(get(), get()) }
        factory<UseCases.GetTriggerInfo> { GetTriggerInfoUseCase(get()) }
        factory<UseCases.GetTable> { GetTableUseCase(get(), get(qualifier = Qualifiers.TABLES)) }
        factory<UseCases.GetView> { GetViewUseCase(get(), get(qualifier = Qualifiers.VIEWS)) }
        factory<UseCases.GetTrigger> { GetTriggerUseCase(get(), get(qualifier = Qualifiers.TRIGGERS)) }
        factory<UseCases.DropTableContent> { DropTableContentUseCase(get(), get(qualifier = Qualifiers.TABLES)) }
        factory<UseCases.DropView> { DropViewUseCase(get(), get(qualifier = Qualifiers.VIEWS)) }
        factory<UseCases.DropTrigger> { DropTriggerUseCase(get(), get(qualifier = Qualifiers.TRIGGERS)) }
    }

    private fun pragma() = module {
        factory<Mappers.Pragma> { PragmaMapper() }
        factory<Converters.Pragma> { PragmaConverter(get()) }
        factory<Control.Pragma> { PragmaControl(get(), get()) }

        factory<Repositories.Pragma> { PragmaRepository(get(), get(), get(), get(), get()) }

        factory<UseCases.GetTablePragma> { GetTablePragmaUseCase(get(), get()) }
        factory<UseCases.GetForeignKeys> { GetForeignKeysUseCase(get(), get()) }
        factory<UseCases.GetIndexes> { GetIndexesUseCase(get(), get()) }
    }

    private fun rawQuery() = module {
        factory<Repositories.RawQuery> { RawRepository(get(), get(), get(), get()) }

        factory<UseCases.GetRawQueryHeaders> { GetRawQueryHeadersUseCase(get(), get()) }
        factory<UseCases.GetRawQuery> { GetRawQueryUseCase(get(), get()) }
        factory<UseCases.GetAffectedRows> { GetAffectedRowsUseCase(get(), get()) }
    }

    private fun history() = module {
        factory<Mappers.Execution> { ExecutionMapper() }
        factory<Mappers.History> { HistoryMapper(get()) }
        factory<Converters.History> { HistoryConverter() }
        factory<Control.History> { HistoryControl(get(), get()) }

        factory<Repositories.History> {
            HistoryRepository(get(), get(), get(), get(), get(), get())
        }

        factory<UseCases.GetHistory> { GetHistoryUseCase(get()) }
        factory<UseCases.SaveExecution> { SaveExecutionUseCase(get()) }
        factory<UseCases.ClearHistory> { ClearHistoryUseCase(get()) }
        factory<UseCases.RemoveExecution> { RemoveExecutionUseCase(get()) }
        factory<UseCases.GetSimilarExecution> { GetSimilarExecutionUseCase(get()) }
    }

    private fun shared() = module {
        factory<Mappers.Content> { ContentMapper(get()) }
        factory<Mappers.BlobPreviewMode> { BlobPreviewModeMapper() }
        factory<Mappers.TruncateMode> { TruncateModeMapper() }
        factory<Mappers.Cell> { CellMapper(get()) }

        factory<Converters.Content> { ContentConverter(get()) }
        factory<Converters.Sort> { SortConverter() }
        factory<Converters.TruncateMode> { TruncateModeConverter() }
        factory<Converters.BlobPreview> { BlobPreviewConverter() }

        factory<Control.Content> { ContentControl(get(), get()) }
    }
}
