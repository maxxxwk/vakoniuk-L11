package com.maxxxwk.hometaskl11

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.widget.Toast

class UserTapReceiver : BroadcastReceiver() {

    companion object {
        private const val USER_TAP_RECEIVER_ACTION = "USER_TAP_RECEIVER_ACTION"

        fun getIntentFilter() = IntentFilter(USER_TAP_RECEIVER_ACTION)

        fun start(context: Context) {
            val intent = Intent(USER_TAP_RECEIVER_ACTION)
            context.sendBroadcast(intent)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val message = "${this::class.java.simpleName} - user tap on button"
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
        Log.d(this::class.java.simpleName, message)
    }
}