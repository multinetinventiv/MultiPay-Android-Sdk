package com.inventiv.multipaysdk

import com.inventiv.multipaysdk.data.model.response.SingleWalletResponse
import com.inventiv.multipaysdk.data.model.response.UnselectWalletResponse

interface MultiPaySdkListener {
    fun onTokenReceived(token: String) {}
    fun onSingeWalletReceived(singleWallet: SingleWalletResponse) {}
    fun onUnSelectWalletReceived(unSelectWallet: UnselectWalletResponse?) {}
    fun onServiceError(error: String?, code: Int) {}
    fun onMultiPaySdkClosed() {}
    fun onConfirmPaymentReceived(sign: String, transferServerRefNo: String) {}
}