package com.emapps.hamburgertest.data.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ConnectivityServiceTest {

    private lateinit var connectivityService: ConnectivityService
    private val context = mockk<Context>()
    private val connectivityManager = mockk<ConnectivityManager>()
    private val networkCallbackSlot = slot<ConnectivityManager.NetworkCallback>()

    @Before
    fun setUp() {
        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager
        every { connectivityManager.registerNetworkCallback(any<NetworkRequest>(), capture(networkCallbackSlot)) } returns Unit
        every { connectivityManager.unregisterNetworkCallback(any<ConnectivityManager.NetworkCallback>()) } returns Unit
        connectivityService = ConnectivityService(context)
    }

    @Test
    fun `when network is initially available, emits true`() = runTest {
        val network = mockk<Network>()
        val capabilities = mockk<NetworkCapabilities>()
        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns capabilities
        every { capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true

        val result = connectivityService.isNetworkAvailable.take(1).toList()

        assertEquals(listOf(true), result)
    }

    @Test
    fun `when network is initially unavailable, emits false`() = runTest {

        every { connectivityManager.activeNetwork } returns null


        val result = connectivityService.isNetworkAvailable.take(1).toList()

        assertEquals(listOf(false), result)
    }

    @Test
    fun `when network becomes unavailable, emits true`() = runTest {

        every { connectivityManager.activeNetwork } returns null
        val network = mockk<Network>()
        val capabilities = mockk<NetworkCapabilities>()

        every { connectivityManager.getNetworkCapabilities(network) } returns capabilities
        every { capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true

        val result = connectivityService.isNetworkAvailable.take(2).toList()
        networkCallbackSlot.captured.onAvailable(network)

        assertEquals(listOf(false, true), result)
    }

    @Test
    fun `when network is lost, emits false`() = runTest {

        val network = mockk<Network>()
        val capabilities = mockk<NetworkCapabilities>()

        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns capabilities
        every { capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true

        val result = connectivityService.isNetworkAvailable.take(2).toList()
        networkCallbackSlot.captured.onLost(network)

        assertEquals(listOf(true, false), result)
    }
}