package com.inventiv.multipaysdk.sample

import android.app.Application
import com.inventiv.multipaysdk.MultiPaySdk
import com.inventiv.multipaysdk.data.model.type.Language

class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        MultiPaySdk.init(
            context = this,
            walletAppToken = "",
            paymentAppToken = "",
            language = Language.TR
        )
    }
}