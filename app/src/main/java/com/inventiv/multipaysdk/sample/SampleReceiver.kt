package com.inventiv.multipaysdk.sample

import com.inventiv.multipaysdk.MultiPaySdkListener
import com.inventiv.multipaysdk.receiver.MultiPaySdkReceiver

class SampleReceiver(
    private val multipaySdkListener: MultiPaySdkListener
) : MultiPaySdkReceiver() {

    override fun onTokenReceived(token: String) {
        multipaySdkListener.onTokenReceived(token)
    }

    override fun sdkClosed() {
        multipaySdkListener.onMultiPaySdkClosed()
    }
}