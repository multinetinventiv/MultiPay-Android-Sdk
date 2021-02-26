package com.inventiv.multipaysdk.data.model.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class ConfirmPaymentRequest(
    @field:SerializedName("walletToken")
    val walletToken: String,
    @field:SerializedName("requestId")
    val requestId: String,
    @field:SerializedName("merchantReferenceNumber")
    val merchantReferenceNumber: String,
    @field:SerializedName("terminalReferenceNumber")
    val terminalReferenceNumber: String,
    @field:SerializedName("transferReferenceNumber")
    val transferReferenceNumber: String,
    @field:SerializedName("transactionDetails")
    val transactionDetails: List<TransactionDetail>,
    @field:SerializedName("sign")
    val sign: String
) : BaseRequest(), Parcelable