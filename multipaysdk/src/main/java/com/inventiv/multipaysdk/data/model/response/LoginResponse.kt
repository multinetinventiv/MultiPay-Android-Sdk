package com.inventiv.multipaysdk.data.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class LoginResponse(
    @field:SerializedName("verificationCode")
    var verificationCode: String?,
    @field:SerializedName("remainingTime")
    var remainingTime: Int?,
    @field:SerializedName("gsm")
    var gsm: String?
) : Parcelable, BaseResponse