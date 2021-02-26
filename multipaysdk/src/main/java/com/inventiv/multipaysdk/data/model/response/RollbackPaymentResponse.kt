package com.inventiv.multipaysdk.data.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class RollbackPaymentResponse(
    @field:SerializedName("sign")
    val sign: String,
    @field:SerializedName("rollbackServerReferenceNumber")
    val rollbackServerReferenceNumber: String
) : BaseResponse, Parcelable