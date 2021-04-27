package com.infinum.dbinspector.shared

import com.infinum.dbinspector.ui.shared.base.BaseViewModel

internal abstract class BaseViewModelTest : BaseTest() {

    abstract val viewModel: BaseViewModel
}

/*
/*
 * Copyright (c) Koninklijke Philips Electronics N.V. 2021
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the
 * prior written consent of the copyright holder.
 */
package com.philips.coffee.viewmodels.wifi
import com.philips.cdp.dicommclient.request.Error
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy
import com.philips.coffee.data.models.local.params.SessionParams
import com.philips.coffee.data.models.local.params.hal.DeviceDiscoveryParams
import com.philips.coffee.data.models.local.params.hal.ExchangeTokensParams
import com.philips.coffee.domain.Repositories
import com.philips.coffee.domain.UseCases
import com.philips.coffee.domain.usecases.wifi.GetDiscoveredApplianceUseCase
import com.philips.coffee.shared.BaseViewModelTest
import com.philips.coffee.shared.CoroutinesTestRule
import com.philips.coffee.shared.InstantExecutorRule
import com.philips.coffee.ui.shared.base.BaseViewModel
import com.philips.coffee.ui.wifi_setup.wifi_setup_discovery.WifiSetupDiscoveryEvent
import com.philips.coffee.ui.wifi_setup.wifi_setup_discovery.WifiSetupDiscoveryViewModel
import com.philips.ka.oneka.communication.library.WifiApplianceDiscoveryResponse
import com.philips.ka.oneka.communication.library.WifiException
import com.philips.ka.oneka.communication.library.device.wifi.WifiAppliance
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
@ExperimentalCoroutinesApi
@ExtendWith(MockitoExtension::class, CoroutinesTestRule::class, InstantExecutorRule::class)
class WifiSetupDiscoveryViewModelTest : BaseViewModelTest<Nothing, WifiSetupDiscoveryEvent>() {
    private lateinit var viewModel: WifiSetupDiscoveryViewModel
    override fun provideViewModel(): BaseViewModel<Nothing, WifiSetupDiscoveryEvent> = viewModel
    lateinit var getDiscoveredAppliance: UseCases.GetDiscoveredAppliance
    @Mock
    lateinit var deviceDiscoveryRepository: Repositories.DeviceDiscovery
    @Mock
    lateinit var validateLastSignOnProperty: UseCases.ValidateLastSignOnProperty
    @Mock
    lateinit var handleInsecureConnection: UseCases.HandleInsecureConnection
    @Mock
    lateinit var exchangeTokens: UseCases.ExchangeTokens
    @Mock
    lateinit var syncApplianceWithBackend: UseCases.SyncApplianceWithBackend
    @Mock
    lateinit var saveHsdpAuthCode: UseCases.SaveHsdpAuthCode
    @Mock
    lateinit var stopDeviceDiscovery: UseCases.StopDeviceDiscovery
    @Mock
    lateinit var communicationStrategy: CommunicationStrategy
    @BeforeEach
    override fun setup() {
        getDiscoveredAppliance = GetDiscoveredApplianceUseCase(
            deviceDiscoveryRepository,
            testCoroutineDispatcher
        )
        viewModel = WifiSetupDiscoveryViewModel(
            getDiscoveredAppliance,
            validateLastSignOnProperty,
            handleInsecureConnection,
            exchangeTokens,
            syncApplianceWithBackend,
            saveHsdpAuthCode,
            stopDeviceDiscovery
        )
    }
    @Test
    fun `When discovered appliance is found, stop discovery, validate signon property and start hsdp browser flow`() =
        testCoroutineDispatcher.runBlockingTest {
            // Given
            `when`(deviceDiscoveryRepository.fetch(anyKotlin())).thenReturn(
                WifiApplianceDiscoveryResponse.Found(mock(WifiAppliance::class.java))
            )
            stopDiscoveryValidateSignonAndStartHsdpBrowserFlow(this)
        }
    @Test
    fun `When discovered appliance is updated, stop discovery, validate signon property and start hsdp browser flow`() =
        testCoroutineDispatcher.runBlockingTest {
            // Given
            `when`(deviceDiscoveryRepository.fetch(anyKotlin())).thenReturn(
                WifiApplianceDiscoveryResponse.Updated(mock(WifiAppliance::class.java))
            )
            stopDiscoveryValidateSignonAndStartHsdpBrowserFlow(this)
        }
    private suspend fun stopDiscoveryValidateSignonAndStartHsdpBrowserFlow(scope: TestCoroutineScope) {
        `when`(stopDeviceDiscovery()).thenReturn(Unit)
        `when`(validateLastSignOnProperty()).thenReturn(Unit)
        // When
        viewModel.discoverAppliance()
        scope.advanceUntilIdle()
        // Then
        verify(stopDeviceDiscovery).invoke()
        verify(validateLastSignOnProperty).invoke()
        assertEventIsEqualToActual(WifiSetupDiscoveryEvent.StartHsdpBrowserFlow)
    }
    @Test
    fun `When discovered appliance is not found, show discovery error`() =
        testCoroutineDispatcher.runBlockingTest {
            // Given
            `when`(deviceDiscoveryRepository.fetch(DeviceDiscoveryParams)).thenReturn(
                WifiApplianceDiscoveryResponse.Lost(mock(WifiAppliance::class.java))
            )
            // When
            viewModel.discoverAppliance()
            // Then
            assertEventIsEqualToActual(WifiSetupDiscoveryEvent.DiscoveryError)
        }
    @Test
    fun `After HSDP browser flow, discovery is successful if tokens are exchanged and appliance synced with backend`() =
        testCoroutineDispatcher.runBlockingTest {
            // Given
            `when`(saveHsdpAuthCode(SessionParams.SaveHsdpAuthCode("code"))).thenReturn(Unit)
            `when`(exchangeTokens(ExchangeTokensParams(true))).thenReturn(Unit)
            `when`(syncApplianceWithBackend()).thenReturn(Unit)
            // When
            viewModel.saveHsdpAuthCode("code")
            // Then
            verify(saveHsdpAuthCode).invoke(anyKotlin())
            verify(exchangeTokens).invoke(anyKotlin())
            verify(syncApplianceWithBackend).invoke()
            assertEventIsEqualToActual(WifiSetupDiscoveryEvent.Connected)
        }
    @Test
    fun `After HSDP browser flow, discovery is not successful if tokens exchange fails`() =
        testCoroutineDispatcher.runBlockingTest {
            // Given
            `when`(saveHsdpAuthCode(SessionParams.SaveHsdpAuthCode("code"))).thenReturn(Unit)
            doAnswer { throw WifiException(error = Error.REQUEST_UNAUTHORIZED) }.`when`(
                exchangeTokens
            ).invoke(ExchangeTokensParams(true))
            // When
            viewModel.saveHsdpAuthCode("code")
            // Then
            assertEventIsEqualToActual(WifiSetupDiscoveryEvent.HsdpPairingFailed)
        }
    @Test
    fun `After HSDP browser flow, discovery is not successful if sync with backend fails`() =
        testCoroutineDispatcher.runBlockingTest {
            // Given
            `when`(saveHsdpAuthCode(anyKotlin())).thenReturn(Unit)
            `when`(exchangeTokens(anyKotlin())).thenReturn(Unit)
            doAnswer { throw WifiException(error = Error.REQUEST_UNAUTHORIZED) }.`when`(
                syncApplianceWithBackend
            ).invoke()
            // When
            viewModel.saveHsdpAuthCode("code")
            // Then
            assertEventIsEqualToActual(WifiSetupDiscoveryEvent.HsdpPairingFailed)
        }
    @Test
    fun `Insecure connection exception should be handled`() =
        testCoroutineDispatcher.runBlockingTest {
            // Given
            doAnswer { throw WifiException(error = Error.INSECURE_CONNECTION) }.doAnswer { }.`when`(
                deviceDiscoveryRepository
            ).fetch(anyKotlin())
            // When
            viewModel.discoverAppliance()
            // Then
            verify(handleInsecureConnection).invoke()
        }
    @Test
    fun `Any exception when discoverying should show discovery error`() =
        testCoroutineDispatcher.runBlockingTest {
            // Given
            doAnswer { throw TestException() }.doAnswer { }.`when`(
                deviceDiscoveryRepository
            ).fetch(anyKotlin())
            // When
            viewModel.discoverAppliance()
            // Then
            assertEventIsEqualToActual(WifiSetupDiscoveryEvent.DiscoveryError)
        }
}
 */
