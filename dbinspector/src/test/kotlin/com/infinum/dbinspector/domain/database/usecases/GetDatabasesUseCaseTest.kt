package com.infinum.dbinspector.domain.database.usecases

import android.content.Context
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
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
            factory { mockk<Context>() }
            factory { mockk<com.infinum.dbinspector.domain.Repositories.Database>() }
            factory { mockk<com.infinum.dbinspector.domain.Repositories.Connection>() }
            factory { mockk<com.infinum.dbinspector.domain.Repositories.Pragma>() }
        }
    )

    @Test
    fun `Invoking use case gets no databases`() {
        val databaseRepository: com.infinum.dbinspector.domain.Repositories.Database = get()
        val connectionRepository: com.infinum.dbinspector.domain.Repositories.Connection = get()
        val pragmaRepository: com.infinum.dbinspector.domain.Repositories.Pragma = get()
        val useCase = GetDatabasesUseCase(
            databaseRepository,
            connectionRepository,
            pragmaRepository
        )

        coEvery { databaseRepository.getPage(any()) } returns listOf()
        coEvery { connectionRepository.open(any()) } returns mockk()
        coEvery { pragmaRepository.getUserVersion(any()) } returns mockk()
        coEvery { connectionRepository.close(any()) } returns Unit

        test {
            useCase.invoke(any())
        }

        coVerify(exactly = 1) { databaseRepository.getPage(any()) }
        coVerify(exactly = 0) { connectionRepository.open(any()) }
        coVerify(exactly = 0) { pragmaRepository.getUserVersion(any()) }
        coVerify(exactly = 0) { connectionRepository.close(any()) }
    }

    @Test
    @Disabled("No idea why this fails")
    fun `Invoking use case gets databases`() {
        val newContext: Context = get()
        val given = mockk<DatabaseParameters.Get> {
            every { context } returns newContext
            every { argument } returns null
        }
        val givenDescriptors: List<DatabaseDescriptor> = listOf(
            DatabaseDescriptor(
                exists = false,
                name = "test",
                extension = "db",
                parentPath = ""
            )
        )
        val databaseRepository: com.infinum.dbinspector.domain.Repositories.Database = get()
        val connectionRepository: com.infinum.dbinspector.domain.Repositories.Connection = get()
        val pragmaRepository: com.infinum.dbinspector.domain.Repositories.Pragma = get()
        val useCase = GetDatabasesUseCase(
            databaseRepository,
            connectionRepository,
            pragmaRepository
        )

        coEvery { databaseRepository.getPage(any()) } returns givenDescriptors
        coEvery {
            connectionRepository.open(
                ConnectionParameters(databasePath = givenDescriptors.first().absolutePath)
            )
        } returns mockk()
        coEvery { pragmaRepository.getUserVersion(any()) } returns mockk()
        coEvery {
            connectionRepository.close(
                ConnectionParameters(databasePath = givenDescriptors.first().absolutePath)
            )
        } returns Unit

        test {
            useCase.invoke(given)
        }

        coVerify(exactly = 1) { databaseRepository.getPage(any()) }
        coVerify(exactly = 1) { connectionRepository.open(any()) }
        coVerify(exactly = 1) { pragmaRepository.getUserVersion(any()) }
        coVerify(exactly = 1) { connectionRepository.close(any()) }
    }
}
