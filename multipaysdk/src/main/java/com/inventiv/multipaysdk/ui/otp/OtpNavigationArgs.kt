package com.inventiv.multipaysdk.ui.otp

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OtpNavigationArgs(
    @field:SerializedName("verificationCode")
    var verificationCode: String?,
    @field:SerializedName("gsmNumber")
    var gsmNumber: String?,
    @field:SerializedName("remainingTime")
    var remainingTime: Int?
) : Parcelable