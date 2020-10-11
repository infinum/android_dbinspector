package im.dino.dbinspector.ui

import im.dino.dbinspector.domain.Domain
import im.dino.dbinspector.ui.content.table.TableViewModel
import im.dino.dbinspector.ui.content.trigger.TriggerViewModel
import im.dino.dbinspector.ui.content.view.ViewViewModel
import im.dino.dbinspector.ui.databases.DatabaseViewModel
import im.dino.dbinspector.ui.databases.edit.EditViewModel
import im.dino.dbinspector.ui.pragma.PragmaViewModel
import im.dino.dbinspector.ui.pragma.foreignkeys.ForeignKeysViewModel
import im.dino.dbinspector.ui.pragma.indexes.IndexViewModel
import im.dino.dbinspector.ui.pragma.tableinfo.TableInfoViewModel
import im.dino.dbinspector.ui.schema.SchemaViewModel
import im.dino.dbinspector.ui.schema.tables.TablesViewModel
import im.dino.dbinspector.ui.schema.triggers.TriggersViewModel
import im.dino.dbinspector.ui.schema.views.ViewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object Presentation {

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