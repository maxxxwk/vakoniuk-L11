package com.maxxxwk.hometaskl11

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast

class ConnectionStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager?
        val networkInfo = connectivityManager?.activeNetworkInfo
        var connectionState = false
        networkInfo?.let {
            connectionState = it.isConnected
        }
        context?.let {
            when (connectionState) {
                true -> {
                    Toast.makeText(it, "Connection enabled", Toast.LENGTH_SHORT).show()
                }
                false -> {
                    Toast.makeText(it, "Connection is disabled", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}