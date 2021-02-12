package com.inventiv.multipaysdk

import android.content.Context
import androidx.annotation.StringRes
import com.google.gson.Gson
import com.inventiv.multipaysdk.data.api.ApiService
import com.inventiv.multipaysdk.data.api.NetworkManager
import com.inventiv.multipaysdk.data.api.VolleyManager
import com.inventiv.multipaysdk.data.api.callback.NetworkCallback
import com.inventiv.multipaysdk.data.api.error.ApiError
import com.inventiv.multipaysdk.data.model.request.TransactionDetail
import com.inventiv.multipaysdk.data.model.response.*
import com.inventiv.multipaysdk.data.model.type.Language
import com.inventiv.multipaysdk.util.Logger
import com.inventiv.multipaysdk.util.getLanguage

internal class MultiPaySdkComponent(
    private val appContext: Context,
    private var walletAppToken: String,
    private var paymentAppToken: String,
    private var clientReferenceNo: String,
    private val environment: Environment,
    private var language: Language?
) {
    private val volleyManager: VolleyManager
    private val networkManager: NetworkManager
    private val apiService: ApiService

    init {
        this.language = getLanguage(appContext, language)
        volleyManager = VolleyManager(appContext)
        networkManager = NetworkManager(volleyManager, environment)
        apiService = ApiService(networkManager)
        Logger.logging(BuildConfig.DEBUG)
    }

    fun environment() = environment

    fun walletAppToken() = walletAppToken

    fun setWalletAppToken(walletAppToken: String) {
        this.walletAppToken = walletAppToken
    }

    fun paymentAppToken() = paymentAppToken

    fun setPaymentAppToken(paymentAppToken: String) {
        this.paymentAppToken = paymentAppToken
    }

    fun clientReferenceNo() = clientReferenceNo

    fun setClientReferenceNo(clientReferenceNo: String) {
        this.clientReferenceNo = clientReferenceNo
    }

    fun apiService() = apiService

    fun language() = requireNotNull(language)

    fun requireAppContext() = appContext

    fun setLanguage(language: Language?) {
        this.language = getLanguage(appContext, language)
    }

    fun gson() = Gson()

    fun getString(@StringRes resId: Int, vararg args: Any): String {
        return appContext.getString(resId, args)
    }

    fun volleyManager() = volleyManager

    fun singleWallet(walletToken: String, listener: MultiPaySdkListener) {
        apiService.singleWalletRequest(walletToken, object : NetworkCallback<Result> {
            override fun onSuccess(response: Result?) {
                val singleWalletResponse = gson().fromJson<SingleWalletResponse>(
                    response?.result,
                    SingleWalletResponse::class.java
                )
                listener.onSingeWalletReceived(singleWalletResponse)
            }

            override fun onError(error: ApiError) {
                listener.onServiceError(error.message, error.errorCode)
            }
        })
    }

    fun unselectWallet(walletToken: String, listener: MultiPaySdkListener) {
        apiService.unselectWalletRequest(walletToken, object : NetworkCallback<Result> {
            override fun onSuccess(response: Result?) {
                val unselectWalletResponse = gson().fromJson<UnselectWalletResponse>(
                    response?.result,
                    UnselectWalletResponse::class.java
                )
                listener.onUnSelectWalletReceived(unselectWalletResponse)
            }

            override fun onError(error: ApiError) {
                listener.onServiceError(error.message, error.errorCode)
            }
        })
    }

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
        apiService.confirmPaymentRequest(
            walletToken,
            requestId,
            merchantReferenceNumber,
            terminalReferenceNumber,
            transferReferenceNumber,
            transactionDetails,
            sign,
            object : NetworkCallback<Result> {
                override fun onSuccess(response: Result?) {
                    val confirmPaymentResponse = gson().fromJson<ConfirmPaymentResponse>(
                        response?.result,
                        ConfirmPaymentResponse::class.java
                    )
                    listener.onConfirmPaymentReceived(
                        confirmPaymentResponse.sign,
                        confirmPaymentResponse.transferServerRefNo
                    )
                }

                override fun onError(error: ApiError) {
                    listener.onServiceError(error.message, error.errorCode)
                }
            })
    }

    fun rollbackPayment(
        requestId: String,
        sign: String,
        merchantReferenceNumber: String,
        terminalReferenceNumber: String,
        rollbackReferenceNumber: String,
        reason: Int,
        referenceNumberType: Int,
        referenceNumber: String,
        listener: MultiPaySdkListener
    ) {
        apiService.rollbackPaymentRequest(
            requestId,
            sign,
            merchantReferenceNumber,
            terminalReferenceNumber,
            rollbackReferenceNumber,
            reason,
            referenceNumberType,
            referenceNumber,
            object : NetworkCallback<Result> {
                override fun onSuccess(response: Result?) {
                    val rollbackPaymentResponse = gson().fromJson<RollbackPaymentResponse>(
                        response?.result,
                        RollbackPaymentResponse::class.java
                    )
                    listener.onRollbackPaymentReceived(
                        rollbackPaymentResponse.sign,
                        rollbackPaymentResponse.rollbackServerReferenceNumber
                    )
                }

                override fun onError(error: ApiError) {
                    listener.onServiceError(error.message, error.errorCode)
                }
            })
    }
}