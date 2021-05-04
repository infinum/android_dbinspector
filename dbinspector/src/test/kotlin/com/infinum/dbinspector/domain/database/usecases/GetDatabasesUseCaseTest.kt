package com.infinum.dbinspector.domain.database.usecases

import android.content.Context
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get
import org.mockito.kotlin.any

@DisplayName("GetDatabasesUseCase tests")
internal class GetDatabasesUseCaseTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<Context>() }
            single { mockk<Repositories.Database>() }
            single { mockk<Repositories.Connection>() }
            single { mockk<Repositories.Pragma>() }
            factory<UseCases.GetDatabases> { GetDatabasesUseCase(get(), get(), get()) }
        }
    )

    @Test
    fun `Invoking use case gets no databases`() {
        val useCase: UseCases.GetDatabases = get()
        val databaseRepository: Repositories.Database = get()
        val connectionRepository: Repositories.Connection = get()
        val pragmaRepository: Repositories.Pragma = get()

        coEvery { useCase.invoke(any()) } returns mockk()
        coEvery { databaseRepository.getPage(any()) } returns listOf()
        coEvery { connectionRepository.open(any()) } returns mockk()
        coEvery { pragmaRepository.getUserVersion(any()) } returns mockk()
        coEvery { connectionRepository.close(any()) } returns mockk()

        launch {
            useCase.invoke(any())
        }

        coVerify(exactly = 1) { databaseRepository.getPage(any()) }
        coVerify(exactly = 0) { connectionRepository.open(any()) }
        coVerify(exactly = 0) { pragmaRepository.getUserVersion(any()) }
        coVerify(exactly = 0) { connectionRepository.close(any()) }
    }

    @Test
    @Disabled("Connection open and close are not called")
    fun `Invoking use case gets databases`() {
        val useCase: UseCases.GetDatabases = get()
        val databaseRepository: Repositories.Database = get()
        val connectionRepository: Repositories.Connection = get()
        val pragmaRepository: Repositories.Pragma = get()

        val given: DatabaseParameters.Get = DatabaseParameters.Get(
            context = get(),
            argument = null
        )
        val givenDescriptors: List<DatabaseDescriptor> = listOf(
            DatabaseDescriptor(
                exists = false,
                name = "test",
                extension = "db",
                parentPath = ""
            )
        )

        coEvery { useCase.invoke(given) } returns mockk()
        coEvery { databaseRepository.getPage(any()) } returns givenDescriptors
        coEvery { connectionRepository.open(ConnectionParameters(databasePath = givenDescriptors.first().absolutePath)) } returns mockk()
        coEvery { pragmaRepository.getUserVersion(any()) } returns mockk()
        coEvery { connectionRepository.close(ConnectionParameters(databasePath = givenDescriptors.first().absolutePath)) } returns mockk()

        launch {
            useCase.invoke(given)
        }

        coVerify(exactly = 1) { databaseRepository.getPage(any()) }
        coVerify(exactly = 1) { connectionRepository.open(any()) }
        coVerify(exactly = 1) { pragmaRepository.getUserVersion(any()) }
        coVerify(exactly = 1) { connectionRepository.close(any()) }
    }
}
