package com.infinum.dbinspector.shared

import androidx.annotation.CallSuper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.module.Module
import org.koin.test.KoinTest
import org.koin.test.junit5.KoinTestExtension
import org.koin.test.junit5.mock.MockProviderExtension
import org.mockito.Mockito

internal abstract class BaseTest : KoinTest {

    internal val testScope = lazyOf(TestCoroutineScope()).value
    private val testDispatcher = lazyOf(TestCoroutineDispatcher()).value

    abstract fun modules(): List<Module>

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(this@BaseTest.modules())
    }

    @JvmField
    @RegisterExtension
    val mockProvider = MockProviderExtension.create { clazz ->
        Mockito.mock(clazz.java)
    }

    protected fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) =
        testDispatcher.runBlockingTest(block)

    protected fun launch(block: suspend CoroutineScope.() -> Unit) {
        testScope.launch { block.invoke(this) }
    }

    protected suspend fun <T> test(block: suspend CoroutineScope.() -> T) =
        withContext(context = testDispatcher) { block.invoke(this) }

    protected fun advanceUntilIdle() = testDispatcher.advanceUntilIdle()

    @BeforeEach
    @CallSuper
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    @CallSuper
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        testScope.cleanupTestCoroutines()
    }
}
