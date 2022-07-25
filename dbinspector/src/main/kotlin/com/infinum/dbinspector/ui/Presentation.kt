package com.infinum.dbinspector.ui

import android.annotation.SuppressLint
import android.content.Context
import com.infinum.dbinspector.di.LibraryKoin
import com.infinum.dbinspector.domain.Domain
import com.infinum.dbinspector.ui.content.table.TableViewModel
import com.infinum.dbinspector.ui.content.trigger.TriggerViewModel
import com.infinum.dbinspector.ui.content.view.ViewViewModel
import com.infinum.dbinspector.ui.databases.DatabaseViewModel
import com.infinum.dbinspector.ui.databases.remove.RemoveDatabaseViewModel
import com.infinum.dbinspector.ui.databases.rename.RenameDatabaseViewModel
import com.infinum.dbinspector.ui.edit.EditViewModel
import com.infinum.dbinspector.ui.edit.history.HistoryViewModel
import com.infinum.dbinspector.ui.logger.Logger
import com.infinum.dbinspector.ui.pragma.PragmaViewModel
import com.infinum.dbinspector.ui.pragma.foreignkeys.ForeignKeysViewModel
import com.infinum.dbinspector.ui.pragma.indexes.IndexViewModel
import com.infinum.dbinspector.ui.pragma.tableinfo.TableInfoViewModel
import com.infinum.dbinspector.ui.schema.SchemaViewModel
import com.infinum.dbinspector.ui.schema.tables.TablesViewModel
import com.infinum.dbinspector.ui.schema.triggers.TriggersViewModel
import com.infinum.dbinspector.ui.schema.views.ViewsViewModel
import com.infinum.dbinspector.ui.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

@SuppressLint("StaticFieldLeak")
internal object Presentation {

    object Constants {

        object Keys {
            const val DATABASE_PATH = "KEY_DATABASE_PATH"
            const val DATABASE_NAME = "KEY_DATABASE_NAME"
            const val SCHEMA_NAME = "KEY_SCHEMA_NAME"
            const val SHOULD_REFRESH = "KEY_SHOULD_REFRESH"
            const val ERROR_MESSAGE = "KEY_ERROR_MESSAGE"
            const val REMOVE_DATABASE_DESCRIPTOR = "KEY_REMOVE_DATABASE_DESCRIPTOR"
            const val RENAME_DATABASE_DESCRIPTOR = "KEY_RENAME_DATABASE_DESCRIPTOR"
            const val REMOVE_DATABASE = "KEY_REMOVE_DATABASE"
            const val RENAME_DATABASE = "KEY_RENAME_DATABASE"
            const val DROP_MESSAGE = "KEY_DROP_MESSAGE"
            const val DROP_NAME = "KEY_DROP_NAME"
            const val DROP_CONTENT = "KEY_DROP_CONTENT"
            const val PREVIEW_CELL = "KEY_PREVIEW_CELL"
        }

        object Limits {
            const val PAGE_SIZE = 100
            const val INITIAL_PAGE = 1
            const val DEBOUNCE_MILIS = 300L
        }

        object Settings {
            const val LINES_LIMIT_MINIMUM = 1
            const val LINES_LIMIT_MAXIMUM = 100
        }
    }

    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }

    fun setLogger(logger: Logger? = null) {
        logger?.let { LibraryKoin.setLibraryLogger(it) }
    }

    fun applicationContext(): Context {
        if (this::context.isInitialized) {
            return context.applicationContext
        } else {
            throw NullPointerException("Presentation context has not been initialized.")
        }
    }

    fun modules(): List<Module> =
        Domain.modules().plus(
            listOf(
                viewModels()
            )
        )

    private fun viewModels() = module {
        viewModel { DatabaseViewModel(get(), get(), get()) }
        viewModel { RenameDatabaseViewModel(get()) }
        viewModel { RemoveDatabaseViewModel(get()) }

        viewModel { SettingsViewModel(get(), get(), get(), get(), get(), get(), get()) }

        viewModel { SchemaViewModel(get(), get()) }

        viewModel { TablesViewModel(get(), get(), get()) }
        viewModel { ViewsViewModel(get(), get(), get()) }
        viewModel { TriggersViewModel(get(), get(), get()) }

        viewModel { TableViewModel(get(), get(), get(), get(), get()) }
        viewModel { ViewViewModel(get(), get(), get(), get(), get()) }
        viewModel { TriggerViewModel(get(), get(), get(), get(), get()) }

        viewModel { PragmaViewModel(get(), get()) }

        viewModel { TableInfoViewModel(get(), get(), get()) }
        viewModel { ForeignKeysViewModel(get(), get(), get()) }
        viewModel { IndexViewModel(get(), get(), get()) }

        viewModel { EditViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
        viewModel { HistoryViewModel(get(), get(), get()) }
    }
}
