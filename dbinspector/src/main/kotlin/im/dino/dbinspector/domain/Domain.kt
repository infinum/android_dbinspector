package im.dino.dbinspector.domain

import im.dino.dbinspector.data.Data
import im.dino.dbinspector.domain.connection.ConnectionRepository
import im.dino.dbinspector.domain.connection.interactors.CloseConnectionInteractor
import im.dino.dbinspector.domain.connection.interactors.OpenConnectionInteractor
import im.dino.dbinspector.domain.database.DatabaseRepository
import im.dino.dbinspector.domain.database.interactors.CopyDatabaseInteractor
import im.dino.dbinspector.domain.database.interactors.GetDatabasesInteractor
import im.dino.dbinspector.domain.database.interactors.ImportDatabasesInteractor
import im.dino.dbinspector.domain.database.interactors.RemoveDatabaseInteractor
import im.dino.dbinspector.domain.database.interactors.RenameDatabaseInteractor
import im.dino.dbinspector.domain.pragma.PragmaRepository
import im.dino.dbinspector.domain.pragma.interactors.GetForeignKeysInteractor
import im.dino.dbinspector.domain.pragma.interactors.GetIndexesInteractor
import im.dino.dbinspector.domain.pragma.interactors.GetTableInfoInteractor
import im.dino.dbinspector.domain.pragma.interactors.GetUserVersionInteractor
import im.dino.dbinspector.domain.schema.table.TableRepository
import im.dino.dbinspector.domain.schema.table.interactors.DropTableContentByNameInteractor
import im.dino.dbinspector.domain.schema.table.interactors.GetTableByNameInteractor
import im.dino.dbinspector.domain.schema.table.interactors.GetTablesInteractor
import im.dino.dbinspector.domain.schema.trigger.TriggerRepository
import im.dino.dbinspector.domain.schema.trigger.interactors.DropTriggerByNameInteractor
import im.dino.dbinspector.domain.schema.trigger.interactors.GetTriggerByNameInteractor
import im.dino.dbinspector.domain.schema.trigger.interactors.GetTriggersInteractor
import im.dino.dbinspector.domain.schema.view.ViewRepository
import im.dino.dbinspector.domain.schema.view.interactors.DropViewByNameInteractor
import im.dino.dbinspector.domain.schema.view.interactors.GetViewByNameInteractor
import im.dino.dbinspector.domain.schema.view.interactors.GetViewsInteractor
import im.dino.dbinspector.domain.connection.usecases.CloseConnectionUseCase
import im.dino.dbinspector.domain.database.usecases.CopyDatabaseUseCase
import im.dino.dbinspector.domain.schema.table.usecases.DropTableContentUseCase
import im.dino.dbinspector.domain.schema.trigger.usecases.DropTriggerUseCase
import im.dino.dbinspector.domain.schema.view.usecases.DropViewUseCase
import im.dino.dbinspector.domain.database.usecases.GetDatabasesUseCase
import im.dino.dbinspector.domain.pragma.usecases.GetTableInfoUseCase
import im.dino.dbinspector.domain.schema.table.usecases.GetTableUseCase
import im.dino.dbinspector.domain.schema.table.usecases.GetTablesUseCase
import im.dino.dbinspector.domain.pragma.usecases.GetTriggerInfoUseCase
import im.dino.dbinspector.domain.schema.trigger.usecases.GetTriggerUseCase
import im.dino.dbinspector.domain.schema.trigger.usecases.GetTriggersUseCase
import im.dino.dbinspector.domain.schema.view.usecases.GetViewUseCase
import im.dino.dbinspector.domain.schema.view.usecases.GetViewsUseCase
import im.dino.dbinspector.domain.database.usecases.ImportDatabasesUseCase
import im.dino.dbinspector.domain.connection.usecases.OpenConnectionUseCase
import im.dino.dbinspector.domain.database.usecases.RemoveDatabaseUseCase
import im.dino.dbinspector.domain.database.usecases.RenameDatabaseUseCase
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
                schema(),
                pragma()
            )
        )

    private fun database() = module {
        factory<Interactors.GetDatabases> { GetDatabasesInteractor(get()) }
        factory<Interactors.ImportDatabases> { ImportDatabasesInteractor(get()) }
        factory<Interactors.RemoveDatabase> { RemoveDatabaseInteractor(get()) }
        factory<Interactors.RenameDatabase> { RenameDatabaseInteractor(get()) }
        factory<Interactors.CopyDatabase> { CopyDatabaseInteractor(get()) }

        factory<Repositories.Database> { DatabaseRepository(get(), get(), get(), get(), get()) }

        factory<UseCases.GetDatabases> { GetDatabasesUseCase(get(), get(), get()) }
        factory<UseCases.ImportDatabases> { ImportDatabasesUseCase(get()) }
        factory<UseCases.RemoveDatabase> { RemoveDatabaseUseCase(get()) }
        factory<UseCases.CopyDatabase> { CopyDatabaseUseCase(get()) }
        factory<UseCases.RenameDatabase> { RenameDatabaseUseCase(get()) }
    }

    private fun connection() = module {
        single<Interactors.OpenConnection> { OpenConnectionInteractor(get()) }
        single<Interactors.CloseConnection> { CloseConnectionInteractor(get()) }

        single<Repositories.Connection> { ConnectionRepository(get(), get()) }

        factory<UseCases.OpenConnection> { OpenConnectionUseCase(get()) }
        factory<UseCases.CloseConnection> { CloseConnectionUseCase(get()) }
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

        factory<Repositories.Schema>(qualifier = Qualifiers.TABLES) { TableRepository(get(), get(), get()) }
        factory<Repositories.Schema>(qualifier = Qualifiers.VIEWS) { ViewRepository(get(), get(), get()) }
        factory<Repositories.Schema>(qualifier = Qualifiers.TRIGGERS) { TriggerRepository(get(), get(), get()) }

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
        factory<Interactors.GetUserVersion> { GetUserVersionInteractor(get()) }
        factory<Interactors.GetTableInfo> { GetTableInfoInteractor(get()) }
        factory<Interactors.GetForeignKeys> { GetForeignKeysInteractor(get()) }
        factory<Interactors.GetIndexes> { GetIndexesInteractor(get()) }

        factory<Repositories.Pragma> { PragmaRepository(get(), get(), get(), get()) }
    }
}