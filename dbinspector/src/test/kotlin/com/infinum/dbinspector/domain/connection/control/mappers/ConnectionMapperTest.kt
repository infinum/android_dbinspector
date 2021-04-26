package com.infinum.dbinspector.domain.connection.control.mappers

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.sources.memory.connection.AndroidConnectionSource
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.shared.BaseMapperTest
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.inject

internal class ConnectionMapperTest : BaseMapperTest() {

    override val mapper by inject<Mappers.Connection>()

    override fun modules(): List<Module> = listOf(
        module {
            single<Sources.Memory> { AndroidConnectionSource() }
            single<Mappers.Connection> { ConnectionMapper() }
        }
    )

//    @Test
//    fun `Default local values maps to default domain values`() =
//        launch {
// //            declareMock<Sources.Memory> {
// //                BDDMockito.given(openConnection("")).will()
// //            }
//            /*
//            val uuidValue = "UUID"
//            declareMock<Simple.UUIDComponent> {
//                BDDMockito.given(getUUID()).will { uuidValue }
//            }
//             */
//            val resource: URL = this.javaClass.classLoader.getResource("chinook.db")
//            val file = File(resource.path)
// //            doReturn(file).`when`(context).getDatabasePath("mydb.db")
//
//            val source: Sources.Memory = get()
//            val given = source.openConnection(resource.path)
//            val expected = DatabaseConnection(database = given)
//            val actual = test {
//                mapper(given)
//            }
//            assertEquals(expected, actual)
//        }
}
