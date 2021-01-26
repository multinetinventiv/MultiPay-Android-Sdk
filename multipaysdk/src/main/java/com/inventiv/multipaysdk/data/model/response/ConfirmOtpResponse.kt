package com.inventiv.multipaysdk.data.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class ConfirmOtpResponse(
    @field:SerializedName("authToken")
    var authToken: String?
) : Parcelable, BaseResponse