package com.anvesh.autumn3.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build

class ConnectionManager {
    fun checkConnectivity(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network: Network? = connectivityManager.activeNetwork
        if (network != null) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI
            )
        }
        return false
    }
}