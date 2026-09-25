package com.infinum.dbinspector.shared

import androidx.annotation.CallSuper
import com.infinum.dbinspector.ui.shared.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.module.Module
import org.koin.test.KoinTest
import org.koin.test.junit5.KoinTestExtension

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal abstract class BaseTest : KoinTest {

    abstract fun modules(): List<Module>

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(this@BaseTest.modules())
    }

    protected fun blockingTest(block: suspend CoroutineScope.() -> Unit) =
        runBlocking { block.invoke(this) }

    protected fun test(block: suspend TestScope.() -> Unit) =
        runTest { block.invoke(this) }

    /**
     * A fresh dispatcher per test, installed as both Main and the ViewModel IO dispatcher.
     * runTest picks up Main's scheduler, so advanceUntilIdle() drives all ViewModel work
     * deterministically instead of racing real Dispatchers.IO threads.
     */
    @BeforeEach
    @CallSuper
    fun setUp() {
        val testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        BaseViewModel.ioDispatcher = testDispatcher
    }

    @AfterEach
    @CallSuper
    fun cleanUp() {
        BaseViewModel.ioDispatcher = Dispatchers.IO
        Dispatchers.resetMain()
    }
}
