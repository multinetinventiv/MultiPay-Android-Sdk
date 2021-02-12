package com.inventiv.multipaysdk.data.api

import com.inventiv.multipaysdk.data.api.callback.NetworkCallback
import com.inventiv.multipaysdk.data.model.request.*
import com.inventiv.multipaysdk.data.model.response.Result
import com.inventiv.multipaysdk.data.model.singleton.MultiPayUser


internal class ApiService(private val networkManager: NetworkManager) {

    fun loginRequest(
        loginRequest: LoginRequest,
        networkCallback: NetworkCallback<Result>
    ) {
        networkManager.sendRequest(
            request = loginRequest,
            requestPath = "auth/login",
            responseModel = Result::class.java,
            networkCallback = networkCallback
        )
    }

    fun confirmOtpRequest(
        confirmOtp: ConfirmOtp,
        networkCallback: NetworkCallback<Result>
    ) {
        networkManager.sendRequest(
            request = confirmOtp,
            requestPath = "auth/otp/confirm",
            responseModel = Result::class.java,
            networkCallback = networkCallback
        )
    }

    fun walletRequest(
        networkCallback: NetworkCallback<Result>
    ) {
        if (MultiPayUser.walletToken.isNullOrEmpty()) {
            networkManager.sendRequest(
                request = AuthWallet(),
                requestPath = "wallets/list",
                responseModel = Result::class.java,
                networkCallback = networkCallback
            )
        } else {
            networkManager.sendRequest(
                request = Wallet(),
                requestPath = "wallets/list",
                responseModel = Result::class.java,
                networkCallback = networkCallback
            )
        }
    }

    fun addWalletRequest(
        addWalletRequest: AddWalletRequest,
        networkCallback: NetworkCallback<Result>
    ) {
        when (addWalletRequest) {
            is AuthAddWallet -> {
                networkManager.sendRequest(
                    request = addWalletRequest,
                    requestPath = "wallets",
                    responseModel = Result::class.java,
                    networkCallback = networkCallback
                )
            }
            is AddWallet -> {
                networkManager.sendRequest(
                    request = addWalletRequest,
                    requestPath = "wallets",
                    responseModel = Result::class.java,
                    networkCallback = networkCallback
                )
            }
        }
    }

    fun matchWalletRequest(
        walletToken: String,
        networkCallback: NetworkCallback<Result>
    ) {
        if (MultiPayUser.walletToken.isNullOrEmpty()) {
            networkManager.sendRequest(
                request = MatchWallet(walletToken),
                requestPath = "wallets/select",
                responseModel = Result::class.java,
                networkCallback = networkCallback
            )
        } else {
            networkManager.sendRequest(
                request = MatchWallet(walletToken),
                requestPath = "wallets/change",
                responseModel = Result::class.java,
                networkCallback = networkCallback
            )
        }
    }

    fun singleWalletRequest(
        walletToken: String,
        networkCallback: NetworkCallback<Result>
    ) {
        networkManager.sendRequest(
            request = SingleWalletRequest(walletToken = walletToken),
            requestPath = "wallets/single",
            responseModel = Result::class.java,
            networkCallback = networkCallback
        )
    }

    fun unselectWalletRequest(
        walletToken: String,
        networkCallback: NetworkCallback<Result>
    ) {
        networkManager.sendRequest(
            request = UnselectWalletRequest(walletToken = walletToken),
            requestPath = "wallets/unselect",
            responseModel = Result::class.java,
            networkCallback = networkCallback
        )
    }

    fun confirmPaymentRequest(
        walletToken: String,
        requestId: String,
        merchantReferenceNumber: String,
        terminalReferenceNumber: String,
        transferReferenceNumber: String,
        transactionDetails: List<TransactionDetail>,
        sign: String,
        networkCallback: NetworkCallback<Result>
    ) {
        networkManager.sendRequest(
            request = ConfirmPaymentRequest(
                walletToken = walletToken,
                requestId = requestId,
                merchantReferenceNumber = merchantReferenceNumber,
                terminalReferenceNumber = terminalReferenceNumber,
                transferReferenceNumber = transferReferenceNumber,
                transactionDetails = transactionDetails,
                sign = sign
            ),
            requestPath = "payment/confirm",
            responseModel = Result::class.java,
            networkCallback = networkCallback
        )
    }

    fun rollbackPaymentRequest(
        requestId: String,
        sign: String,
        merchantReferenceNumber: String,
        terminalReferenceNumber: String,
        rollbackReferenceNumber: String,
        reason: Int,
        referenceNumberType: Int,
        referenceNumber: String,
        networkCallback: NetworkCallback<Result>
    ) {
        networkManager.sendRequest(
            request = RollbackPaymentRequest(
                requestId = requestId,
                sign = sign,
                merchantReferenceNumber = merchantReferenceNumber,
                terminalReferenceNumber = terminalReferenceNumber,
                rollbackReferenceNumber = rollbackReferenceNumber,
                reason = reason,
                referenceNumberType = referenceNumberType,
                referenceNumber = referenceNumber,
            ),
            requestPath = "payment/rollback",
            responseModel = Result::class.java,
            networkCallback = networkCallback
        )
    }
}