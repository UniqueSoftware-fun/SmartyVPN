package com.smartyvpn.tfsmarty.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * This class is responsible for internet status checking
 */
class CheckInternetConnection {
    /**
     * Check internet status
     * @param context
     * @return: internet connection status
     */

    var connectivityManager: ConnectivityManager? = null
    var connected = false
    fun isOnline(context: Context): Boolean {
        try {
            connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo: NetworkInfo = connectivityManager!!.getActiveNetworkInfo()!!
            connected = true && networkInfo.isAvailable &&
                    networkInfo.isConnected
            return connected
        } catch (e: Exception) {
        }
        return connected
    }

}