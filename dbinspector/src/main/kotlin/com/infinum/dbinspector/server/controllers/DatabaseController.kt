package com.infinum.dbinspector.server.controllers

import android.content.Context
import com.infinum.dbinspector.data.models.remote.DatabaseResponse
import com.infinum.dbinspector.di.LibraryKoin
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters
import com.infinum.dbinspector.extensions.toSha1
import io.ktor.utils.io.core.toByteArray
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class DatabaseController(
    private val context: Context
) : KoinComponent {

    private val getDatabases: UseCases.GetDatabases by inject()
    private val copyDatabase: UseCases.CopyDatabase by inject()
    private val renameDatabase: UseCases.RenameDatabase by inject()
    private val removeDatabase: UseCases.RemoveDatabase by inject()

    override fun getKoin(): Koin = LibraryKoin.koin()

    suspend fun getAll(query: String?): List<DatabaseResponse> = getDatabases(
        DatabaseParameters.Get(
            context = context, argument = query
        )
    ).map { descriptor ->
        DatabaseResponse(
            id = descriptor.absolutePath.toByteArray().toSha1(),
            name = descriptor.name,
            path = descriptor.absolutePath,
            version = descriptor.version
        )
    }

    suspend fun getById(id: String): DatabaseResponse? = getAll(null).find { id == it.id }

    suspend fun copy(id: String): List<DatabaseResponse>? = getDatabases(
        DatabaseParameters.Get(
            context = context, argument = null
        )
    ).find { id == it.absolutePath.toByteArray().toSha1() }?.let {
        copyDatabase(
            DatabaseParameters.Command(
                context = context, databaseDescriptor = it
            )
        )
    }?.map { descriptor ->
        DatabaseResponse(
            id = descriptor.absolutePath.toByteArray().toSha1(),
            name = descriptor.name,
            path = descriptor.absolutePath,
            version = descriptor.version
        )
    }

    suspend fun rename(id: String, newName: String): DatabaseResponse? = getDatabases(
        DatabaseParameters.Get(
            context = context, argument = null
        )
    ).find { id == it.absolutePath.toByteArray().toSha1() }?.let {
        renameDatabase(
            DatabaseParameters.Rename(
                context = context, databaseDescriptor = it, argument = newName
            )
        )
    }?.first()?.let { descriptor ->
        DatabaseResponse(
            id = descriptor.absolutePath.toByteArray().toSha1(),
            name = descriptor.name,
            path = descriptor.absolutePath,
            version = descriptor.version
        )
    }

    suspend fun remove(id: String): List<DatabaseResponse>? = getDatabases(
        DatabaseParameters.Get(
            context = context, argument = null
        )
    ).find { id == it.absolutePath.toByteArray().toSha1() }?.let {
        removeDatabase(
            DatabaseParameters.Command(
                context = context, databaseDescriptor = it
            )
        )
    }?.map { descriptor ->
        DatabaseResponse(
            id = descriptor.absolutePath.toByteArray().toSha1(),
            name = descriptor.name,
            path = descriptor.absolutePath,
            version = descriptor.version
        )
    }
}