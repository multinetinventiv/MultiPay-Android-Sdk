package com.inventiv.multipaysdk

import android.content.Context
import com.inventiv.multipaysdk.data.model.request.TransactionDetail
import com.inventiv.multipaysdk.data.model.singleton.MultiPayUser
import com.inventiv.multipaysdk.data.model.type.Language
import com.inventiv.multipaysdk.ui.splash.SplashActivity

object MultiPaySdk {

    private lateinit var multiPaySdkComponent: MultiPaySdkComponent

    internal fun getComponent() = multiPaySdkComponent

    @JvmOverloads
    @JvmStatic
    fun init(
        context: Context,
        walletAppToken: String,
        paymentAppToken: String,
        userId: String = String(),
        environment: Environment = Environment.PRODUCTION,
        language: Language? = null
    ) {
        this.multiPaySdkComponent =
            MultiPaySdkComponent(
                context,
                walletAppToken,
                paymentAppToken,
                userId,
                environment,
                language
            )
    }

    @JvmStatic
    fun setLanguage(language: Language) = getComponent().setLanguage(language)

    @JvmStatic
    fun startSDKForSubmitConsumer(
        context: Context,
        walletToken: String?
    ) {
        context.startActivity(SplashActivity.newIntent(context))
        MultiPayUser.walletToken = walletToken
    }

    @JvmStatic
    fun getWallet(walletToken: String, listener: MultiPaySdkListener) {
        multiPaySdkComponent.singleWallet(walletToken, listener)
    }

    @JvmStatic
    fun deleteWallet(walletToken: String, listener: MultiPaySdkListener) {
        multiPaySdkComponent.unselectWallet(walletToken, listener)
    }

    @JvmStatic
    fun confirmPayment(
        walletToken: String,
        requestId: String,
        merchantReferenceNumber: String,
        terminalReferenceNumber: String,
        transferReferenceNumber: String,
        transactionDetails: List<TransactionDetail>,
        sign: String,
        listener: MultiPaySdkListener
    ) {
        multiPaySdkComponent.confirmPayment(
            walletToken,
            requestId,
            merchantReferenceNumber,
            terminalReferenceNumber,
            transferReferenceNumber,
            transactionDetails,
            sign,
            listener
        )
    }
}