package com.infinum.dbinspector.ui

import android.content.Context
import com.infinum.dbinspector.domain.Domain
import com.infinum.dbinspector.ui.content.table.TableViewModel
import com.infinum.dbinspector.ui.content.trigger.TriggerViewModel
import com.infinum.dbinspector.ui.content.view.ViewViewModel
import com.infinum.dbinspector.ui.databases.DatabaseViewModel
import com.infinum.dbinspector.ui.databases.edit.EditViewModel
import com.infinum.dbinspector.ui.pragma.PragmaViewModel
import com.infinum.dbinspector.ui.pragma.foreignkeys.ForeignKeysViewModel
import com.infinum.dbinspector.ui.pragma.indexes.IndexViewModel
import com.infinum.dbinspector.ui.pragma.tableinfo.TableInfoViewModel
import com.infinum.dbinspector.ui.schema.SchemaViewModel
import com.infinum.dbinspector.ui.schema.tables.TablesViewModel
import com.infinum.dbinspector.ui.schema.triggers.TriggersViewModel
import com.infinum.dbinspector.ui.schema.views.ViewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object Presentation {

    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context
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
        viewModel { DatabaseViewModel(get(), get(), get(), get(), get()) }
        viewModel { EditViewModel(get(), get()) }

        viewModel { SchemaViewModel(get(), get()) }

        viewModel { TablesViewModel(get()) }
        viewModel { ViewsViewModel(get()) }
        viewModel { TriggersViewModel(get()) }

        viewModel { TableViewModel(get(), get(), get(), get(), get()) }
        viewModel { ViewViewModel(get(), get(), get(), get(), get()) }
        viewModel { TriggerViewModel(get(), get(), get(), get(), get()) }

        viewModel { PragmaViewModel(get(), get()) }

        viewModel { TableInfoViewModel(get()) }
        viewModel { ForeignKeysViewModel(get()) }
        viewModel { IndexViewModel(get()) }
    }
}
