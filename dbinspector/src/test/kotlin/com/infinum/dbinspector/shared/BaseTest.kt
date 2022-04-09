package com.infinum.dbinspector.shared

import androidx.annotation.CallSuper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineExceptionHandler
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.createTestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.module.Module
import org.koin.test.KoinTest
import org.koin.test.junit5.KoinTestExtension

internal abstract class BaseTest : KoinTest {

    internal val testScope = TestScope()
    private val testDispatcher = StandardTestDispatcher()

    abstract fun modules(): List<Module>

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(this@BaseTest.modules())
    }

    protected fun runBlockingTest(block: suspend TestScope.() -> Unit) =
        runTest(testDispatcher) { block.invoke(this) }
//        testDispatcher.runBlockingTest(block)

    protected fun launch(block: suspend CoroutineScope.() -> Unit) {
        testScope.launch { block.invoke(this) }
    }

    protected suspend fun <T> test(block: suspend CoroutineScope.() -> T) =
        withContext(context = testDispatcher) { block.invoke(this) }

    @BeforeEach
    @CallSuper
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    @CallSuper
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
//        testDispatcher.cleanupTestCoroutines()
//        testScope.cleanupTestCoroutines()
    }
}
