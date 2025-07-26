package com.ragdoll.photogalleryapp.presentation.common


import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 *
 * NetworkMonitor is an interface exposing a Flow<Boolean> called isOnline, which emits the device's online status.
 * ConnectivityManagerNetworkMonitor implements this interface.
 * It uses callbackFlow to bridge Android's callback-based network monitoring with Kotlin's Flow API.
 * A NetworkCallback is registered to listen for network availability (onAvailable) and loss (onLost), emitting true or false accordingly.
 * The initial network state is checked and emitted immediately.
 * awaitClose ensures the callback is unregistered when the flow is closed, preventing memory leaks.
 * distinctUntilChanged() ensures only changes in connectivity are emitted, avoiding duplicate values.
 *
 * This approach is lifecycle-aware, efficient, and idiomatic for modern Android development.
 *
 * */

interface NetworkMonitor {
    val isOnline: Flow<Boolean>
}

class ConnectivityManagerNetworkMonitor(
    private val context: Context,

    ) : NetworkMonitor {
    override val isOnline: Flow<Boolean> = callbackFlow {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(false)
            }
        }

        val request = NetworkRequest
            .Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        // Initial state check if the device is currently connected to the internet
        val currentState = connectivityManager.activeNetwork?.let {
            connectivityManager.getNetworkCapabilities(it)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } ?: false
        trySend(currentState)

        // Close the channel when the flow is no longer needed
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()
}