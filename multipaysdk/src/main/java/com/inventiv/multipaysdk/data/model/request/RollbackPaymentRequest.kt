package com.inventiv.multipaysdk.data.model.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class RollbackPaymentRequest(
    @field:SerializedName("requestId")
    val requestId: String,
    @field:SerializedName("sign")
    val sign: String,
    @field:SerializedName("merchantReferenceNumber")
    val merchantReferenceNumber: String,
    @field:SerializedName("terminalReferenceNumber")
    val terminalReferenceNumber: String,
    @field:SerializedName("rollbackReferenceNumber")
    val rollbackReferenceNumber: String,
    @field:SerializedName("reason")
    val reason: Int,
    @field:SerializedName("referenceNumberType")
    val referenceNumberType: Int,
    @field:SerializedName("referenceNumber")
    val referenceNumber: String
) : BaseRequest(), Parcelable {
}