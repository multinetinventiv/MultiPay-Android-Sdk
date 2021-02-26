package com.inventiv.multipaysdk.data.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class ConfirmPaymentResponse(
    @field:SerializedName("sign")
    val sign: String,
    @field:SerializedName("transferServerReferenceNumber")
    val transferServerRefNo: String
) : BaseResponse, Parcelable
