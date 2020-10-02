package im.dino.dbinspector.ui.databases.edit

import android.os.Bundle
import android.text.InputFilter
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorActivityEditBinding
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.shared.bus.EventBus
import im.dino.dbinspector.ui.shared.bus.models.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
internal class EditActivity : AppCompatActivity() {

    lateinit var binding: DbinspectorActivityEditBinding

    private val viewModel: EditViewModel by viewModels()

    private lateinit var databasePath: String
    private lateinit var databaseFilepath: String
    private lateinit var databaseName: String
    private lateinit var databaseExtension: String

    private val nameFilter = InputFilter { source, _, _, _, _, _ ->
        if (source.isEmpty()) return@InputFilter null
        val last = source[source.length - 1]
        val reservedChars = "?:\"*|/\\<>"
        if (reservedChars.indexOf(last) > -1) source.subSequence(0, source.length - 1) else null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DbinspectorActivityEditBinding.inflate(layoutInflater)

        setContentView(binding.root)

        intent.extras?.let {
            databasePath = it.getString(Constants.Keys.DATABASE_PATH)!!
            databaseFilepath = it.getString(Constants.Keys.DATABASE_FILEPATH)!!
            databaseName = it.getString(Constants.Keys.DATABASE_NAME)!!
            databaseExtension = it.getString(Constants.Keys.DATABASE_EXTENSION)!!
            if (databasePath.isBlank().not() && databaseFilepath.isBlank().not() && databaseName.isBlank().not() && databaseExtension.isBlank().not()) {
                setupUi()
            } else {
                showError()
            }
        } ?: showError()
    }

    private fun setupUi() {
        with(binding) {
            toolbar.setNavigationOnClickListener { finish() }
            toolbar.subtitle = databaseName

            nameInput.filters = arrayOf(nameFilter)
            nameInput.setText(databaseName)
            nameInput.doOnTextChanged { text, _, _, _ ->
                if (text.isNullOrBlank()) {
                    nameInputLayout.error = getString(R.string.dbinspector_rename_database_error_blank)
                } else {
                    nameInputLayout.error = null
                }
            }
            nameInput.setOnEditorActionListener { view, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    rename()
                    view.clearFocus()
                }
                false
            }
        }
    }

    private fun showError() {
        with(binding) {
            toolbar.setNavigationOnClickListener { finish() }

            // TODO: push or show error views or Fragment
        }
    }

    private fun rename() {
        val newDatabaseName = binding.nameInput.text?.toString().orEmpty().trim()
        viewModel.rename(
            databasePath,
            "$databaseFilepath/$newDatabaseName.$databaseExtension"
        ) {
            this.databasePath = "$databaseFilepath/$newDatabaseName.$databaseExtension"
            this.databaseName = newDatabaseName
            EventBus.publish(Event.RefreshDatabases())
        }
    }
}