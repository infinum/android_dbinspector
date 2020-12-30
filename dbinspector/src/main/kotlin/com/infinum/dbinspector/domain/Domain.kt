package com.infinum.dbinspector.domain

import com.infinum.dbinspector.data.Data
import com.infinum.dbinspector.domain.connection.ConnectionRepository
import com.infinum.dbinspector.domain.connection.converters.ConnectionConverter
import com.infinum.dbinspector.domain.connection.interactors.CloseConnectionInteractor
import com.infinum.dbinspector.domain.connection.interactors.OpenConnectionInteractor
import com.infinum.dbinspector.domain.connection.usecases.CloseConnectionUseCase
import com.infinum.dbinspector.domain.connection.usecases.OpenConnectionUseCase
import com.infinum.dbinspector.domain.database.DatabaseRepository
import com.infinum.dbinspector.domain.database.converters.DatabaseConverter
import com.infinum.dbinspector.domain.database.interactors.CopyDatabaseInteractor
import com.infinum.dbinspector.domain.database.interactors.GetDatabasesInteractor
import com.infinum.dbinspector.domain.database.interactors.ImportDatabasesInteractor
import com.infinum.dbinspector.domain.database.interactors.RemoveDatabaseInteractor
import com.infinum.dbinspector.domain.database.interactors.RenameDatabaseInteractor
import com.infinum.dbinspector.domain.database.mappers.DatabaseDescriptorMapper
import com.infinum.dbinspector.domain.database.usecases.CopyDatabaseUseCase
import com.infinum.dbinspector.domain.database.usecases.GetDatabasesUseCase
import com.infinum.dbinspector.domain.database.usecases.ImportDatabasesUseCase
import com.infinum.dbinspector.domain.database.usecases.RemoveDatabaseUseCase
import com.infinum.dbinspector.domain.database.usecases.RenameDatabaseUseCase
import com.infinum.dbinspector.domain.pragma.PragmaRepository
import com.infinum.dbinspector.domain.pragma.converters.PragmaConverter
import com.infinum.dbinspector.domain.pragma.interactors.GetForeignKeysInteractor
import com.infinum.dbinspector.domain.pragma.interactors.GetIndexesInteractor
import com.infinum.dbinspector.domain.pragma.interactors.GetTableInfoInteractor
import com.infinum.dbinspector.domain.pragma.interactors.GetUserVersionInteractor
import com.infinum.dbinspector.domain.pragma.mappers.PragmaMapper
import com.infinum.dbinspector.domain.pragma.usecases.GetForeignKeysUseCase
import com.infinum.dbinspector.domain.pragma.usecases.GetIndexesUseCase
import com.infinum.dbinspector.domain.pragma.usecases.GetTableInfoUseCase
import com.infinum.dbinspector.domain.pragma.usecases.GetTablePragmaUseCase
import com.infinum.dbinspector.domain.pragma.usecases.GetTriggerInfoUseCase
import com.infinum.dbinspector.domain.schema.converters.SchemaConverter
import com.infinum.dbinspector.domain.schema.table.TableRepository
import com.infinum.dbinspector.domain.schema.table.interactors.DropTableContentByNameInteractor
import com.infinum.dbinspector.domain.schema.table.interactors.GetTableByNameInteractor
import com.infinum.dbinspector.domain.schema.table.interactors.GetTablesInteractor
import com.infinum.dbinspector.domain.schema.table.usecases.DropTableContentUseCase
import com.infinum.dbinspector.domain.schema.table.usecases.GetTableUseCase
import com.infinum.dbinspector.domain.schema.table.usecases.GetTablesUseCase
import com.infinum.dbinspector.domain.schema.trigger.TriggerRepository
import com.infinum.dbinspector.domain.schema.trigger.interactors.DropTriggerByNameInteractor
import com.infinum.dbinspector.domain.schema.trigger.interactors.GetTriggerByNameInteractor
import com.infinum.dbinspector.domain.schema.trigger.interactors.GetTriggersInteractor
import com.infinum.dbinspector.domain.schema.trigger.usecases.DropTriggerUseCase
import com.infinum.dbinspector.domain.schema.trigger.usecases.GetTriggerUseCase
import com.infinum.dbinspector.domain.schema.trigger.usecases.GetTriggersUseCase
import com.infinum.dbinspector.domain.schema.view.ViewRepository
import com.infinum.dbinspector.domain.schema.view.interactors.DropViewByNameInteractor
import com.infinum.dbinspector.domain.schema.view.interactors.GetViewByNameInteractor
import com.infinum.dbinspector.domain.schema.view.interactors.GetViewsInteractor
import com.infinum.dbinspector.domain.schema.view.usecases.DropViewUseCase
import com.infinum.dbinspector.domain.schema.view.usecases.GetViewUseCase
import com.infinum.dbinspector.domain.schema.view.usecases.GetViewsUseCase
import com.infinum.dbinspector.domain.settings.SettingsRepository
import com.infinum.dbinspector.domain.settings.converters.SettingsConverter
import com.infinum.dbinspector.domain.settings.interactors.GetSettingsInteractor
import com.infinum.dbinspector.domain.settings.interactors.SaveBlobPreviewModeInteractor
import com.infinum.dbinspector.domain.settings.interactors.SaveLinesCountInteractor
import com.infinum.dbinspector.domain.settings.interactors.SaveLinesLimitInteractor
import com.infinum.dbinspector.domain.settings.interactors.SaveTruncateModeInteractor
import com.infinum.dbinspector.domain.settings.mappers.SettingsMapper
import com.infinum.dbinspector.domain.settings.usecases.GetSettingsUseCase
import com.infinum.dbinspector.domain.settings.usecases.SaveBlobPreviewModeUseCase
import com.infinum.dbinspector.domain.settings.usecases.SaveLinesCountUseCase
import com.infinum.dbinspector.domain.settings.usecases.SaveTruncateModeUseCase
import com.infinum.dbinspector.domain.settings.usecases.ToggleLinesLimitUseCase
import com.infinum.dbinspector.domain.shared.converters.BlobPreviewConverter
import com.infinum.dbinspector.domain.shared.converters.SortConverter
import com.infinum.dbinspector.domain.shared.converters.TruncateConverter
import com.infinum.dbinspector.domain.shared.mappers.BlobPreviewModeMapper
import com.infinum.dbinspector.domain.shared.mappers.CellMapper
import com.infinum.dbinspector.domain.shared.mappers.PageMapper
import com.infinum.dbinspector.domain.shared.mappers.TruncateModeMapper
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module

object Domain {

    object Qualifiers {
        val TABLES = StringQualifier("domain.qualifiers.tables")
        val VIEWS = StringQualifier("domain.qualifiers.views")
        val TRIGGERS = StringQualifier("domain.qualifiers.triggers")
    }

    fun modules(): List<Module> =
        Data.modules().plus(
            listOf(
                database(),
                connection(),
                settings(),
                schema(),
                pragma(),
                shared()
            )
        )

    private fun database() = module {
        factory<Interactors.GetDatabases> { GetDatabasesInteractor(get()) }
        factory<Interactors.ImportDatabases> { ImportDatabasesInteractor(get()) }
        factory<Interactors.RemoveDatabase> { RemoveDatabaseInteractor(get()) }
        factory<Interactors.RenameDatabase> { RenameDatabaseInteractor(get()) }
        factory<Interactors.CopyDatabase> { CopyDatabaseInteractor(get()) }

        factory<Mappers.DatabaseDescriptor> { DatabaseDescriptorMapper() }

        factory<Converters.Database> { DatabaseConverter() }

        factory<Repositories.Database> {
            DatabaseRepository(get(), get(), get(), get(), get(), get(), get())
        }

        factory<UseCases.GetDatabases> { GetDatabasesUseCase(get(), get(), get()) }
        factory<UseCases.ImportDatabases> { ImportDatabasesUseCase(get()) }
        factory<UseCases.RemoveDatabase> { RemoveDatabaseUseCase(get()) }
        factory<UseCases.CopyDatabase> { CopyDatabaseUseCase(get()) }
        factory<UseCases.RenameDatabase> { RenameDatabaseUseCase(get()) }
    }

    private fun connection() = module {
        single<Interactors.OpenConnection> { OpenConnectionInteractor(get()) }
        single<Interactors.CloseConnection> { CloseConnectionInteractor(get()) }

        single<Converters.Connection> { ConnectionConverter() }

        single<Repositories.Connection> { ConnectionRepository(get(), get(), get()) }

        factory<UseCases.OpenConnection> { OpenConnectionUseCase(get()) }
        factory<UseCases.CloseConnection> { CloseConnectionUseCase(get()) }
    }

    private fun settings() = module {
        factory<Interactors.GetSettings> { GetSettingsInteractor(get()) }
        factory<Interactors.SaveLinesLimit> { SaveLinesLimitInteractor(get()) }
        factory<Interactors.SaveLinesCount> { SaveLinesCountInteractor(get()) }
        factory<Interactors.SaveTruncateMode> { SaveTruncateModeInteractor(get()) }
        factory<Interactors.SaveBlobPreviewMode> { SaveBlobPreviewModeInteractor(get()) }

        factory<Mappers.Settings> { SettingsMapper(get(), get()) }
        factory<Converters.Settings> { SettingsConverter(get(), get()) }

        factory<Repositories.Settings> {
            SettingsRepository(get(), get(), get(), get(), get(), get(), get())
        }

        factory<UseCases.GetSettings> { GetSettingsUseCase(get()) }
        factory<UseCases.SaveLinesCount> { SaveLinesCountUseCase(get()) }
        factory<UseCases.ToggleLinesLimit> { ToggleLinesLimitUseCase(get()) }
        factory<UseCases.SaveTruncateMode> { SaveTruncateModeUseCase(get()) }
        factory<UseCases.SaveBlobPreviewMode> { SaveBlobPreviewModeUseCase(get()) }
    }

    private fun schema() = module {
        factory<Interactors.GetTables> { GetTablesInteractor(get()) }
        factory<Interactors.GetTableByName> { GetTableByNameInteractor(get()) }
        factory<Interactors.DropTableContentByName> { DropTableContentByNameInteractor(get()) }
        factory<Interactors.GetViews> { GetViewsInteractor(get()) }
        factory<Interactors.GetViewByName> { GetViewByNameInteractor(get()) }
        factory<Interactors.DropViewByName> { DropViewByNameInteractor(get()) }
        factory<Interactors.GetTriggers> { GetTriggersInteractor(get()) }
        factory<Interactors.GetTriggerByName> { GetTriggerByNameInteractor(get()) }
        factory<Interactors.DropTriggerByName> { DropTriggerByNameInteractor(get()) }

        factory<Converters.Schema> { SchemaConverter(get(), get()) }

        factory<Repositories.Schema>(qualifier = Qualifiers.TABLES) {
            TableRepository(get(), get(), get(), get(), get())
        }
        factory<Repositories.Schema>(qualifier = Qualifiers.VIEWS) {
            ViewRepository(get(), get(), get(), get(), get())
        }
        factory<Repositories.Schema>(qualifier = Qualifiers.TRIGGERS) {
            TriggerRepository(get(), get(), get(), get(), get())
        }

        factory<UseCases.GetTables> { GetTablesUseCase(get(), get(qualifier = Qualifiers.TABLES)) }
        factory<UseCases.GetViews> { GetViewsUseCase(get(), get(qualifier = Qualifiers.VIEWS)) }
        factory<UseCases.GetTriggers> { GetTriggersUseCase(get(), get(qualifier = Qualifiers.TRIGGERS)) }
        factory<UseCases.GetTableInfo> { GetTableInfoUseCase(get(), get()) }
        factory<UseCases.GetTriggerInfo> { GetTriggerInfoUseCase(get()) }
        factory<UseCases.GetTable> { GetTableUseCase(get(), get(), get(qualifier = Qualifiers.TABLES)) }
        factory<UseCases.GetView> { GetViewUseCase(get(), get(), get(qualifier = Qualifiers.VIEWS)) }
        factory<UseCases.GetTrigger> { GetTriggerUseCase(get(), get(), get(qualifier = Qualifiers.TRIGGERS)) }
        factory<UseCases.DropTableContent> { DropTableContentUseCase(get(), get(qualifier = Qualifiers.TABLES)) }
        factory<UseCases.DropView> { DropViewUseCase(get(), get(qualifier = Qualifiers.VIEWS)) }
        factory<UseCases.DropTrigger> { DropTriggerUseCase(get(), get(qualifier = Qualifiers.TRIGGERS)) }
    }

    private fun pragma() = module {
        factory<Interactors.GetUserVersion> { GetUserVersionInteractor(get()) }
        factory<Interactors.GetTableInfo> { GetTableInfoInteractor(get()) }
        factory<Interactors.GetForeignKeys> { GetForeignKeysInteractor(get()) }
        factory<Interactors.GetIndexes> { GetIndexesInteractor(get()) }

        factory<Mappers.Pragma> { PragmaMapper() }

        factory<Converters.Pragma> { PragmaConverter(get()) }

        factory<Repositories.Pragma> { PragmaRepository(get(), get(), get(), get(), get(), get()) }

        factory<UseCases.GetTablePragma> { GetTablePragmaUseCase(get(), get()) }
        factory<UseCases.GetForeignKeys> { GetForeignKeysUseCase(get(), get()) }
        factory<UseCases.GetIndexes> { GetIndexesUseCase(get(), get()) }
    }

    private fun shared() = module {
        factory<Mappers.Cell> { CellMapper() }
        factory<Mappers.Page> { PageMapper(get()) }
        factory<Mappers.TruncateMode> { TruncateModeMapper() }
        factory<Mappers.BlobPreviewMode> { BlobPreviewModeMapper() }

        factory<Converters.Sort> { SortConverter() }
        factory<Converters.Truncate> { TruncateConverter() }
        factory<Converters.BlobPreview> { BlobPreviewConverter() }
    }
}
