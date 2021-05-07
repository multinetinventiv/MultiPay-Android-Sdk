package com.inventiv.multipaysdk.sample

import androidx.multidex.MultiDexApplication

class SampleApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        // NORMALLY MultiPaySdk.init method should be called here in Application onCreate method.
        // But it is moved to MultinetWalletActivity in this sample app for demonstration purposes
        /*MultiPaySdk.init(
            context = this,
            walletAppToken = "",
            paymentAppToken = "",
            saltKey = "",
            language = Language.TR
        )*/
    }
}