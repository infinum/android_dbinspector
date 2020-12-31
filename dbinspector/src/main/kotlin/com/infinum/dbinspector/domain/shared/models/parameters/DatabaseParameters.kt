package com.infinum.dbinspector.domain.shared.models.parameters

import android.content.Context
import android.net.Uri
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.domain.shared.base.BaseParameters

internal sealed class DatabaseParameters : BaseParameters {

    data class Get(
        val context: Context,
        val argument: String?
    ) : DatabaseParameters()

    data class Import(
        val context: Context,
        val importUris: List<Uri>
    ) : DatabaseParameters()

    data class Command(
        val context: Context,
        val databaseDescriptor: DatabaseDescriptor
    ) : DatabaseParameters()

    data class Rename(
        val context: Context,
        val databaseDescriptor: DatabaseDescriptor,
        val argument: String
    ) : DatabaseParameters()
}
