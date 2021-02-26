package com.inventiv.multipaysdk.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.inventiv.multipaysdk.util.EXTRA_TOKEN_RECEIVED
import com.inventiv.multipaysdk.util.SDK_CLOSED
import com.inventiv.multipaysdk.util.TOKEN_RECEIVED

abstract class MultiPaySdkReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        when (intent?.action) {
            TOKEN_RECEIVED -> {
                val token = intent.getStringExtra(EXTRA_TOKEN_RECEIVED)
                onTokenReceived(token!!)
            }
            SDK_CLOSED -> {
                sdkClosed()
            }
        }
    }

    protected abstract fun onTokenReceived(token: String)

    protected abstract fun sdkClosed()

}