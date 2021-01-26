package com.inventiv.multipaysdk.data.model.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
internal data class ConfirmOtp(
    @field:SerializedName("verificationCode")
    var verificationCode: String?,
    @field:SerializedName("smsCode")
    var smsCode: String,
    @field:SerializedName("requestId")
    var requestId: String = UUID.randomUUID().toString()
) : Parcelable, BaseRequest()