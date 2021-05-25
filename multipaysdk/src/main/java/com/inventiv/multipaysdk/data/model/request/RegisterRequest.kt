package com.inventiv.multipaysdk.data.model.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
internal data class RegisterRequest(
    @field:SerializedName("name")
    var name: String,
    @field:SerializedName("surname")
    var surname: String,
    @field:SerializedName("email")
    var email: String,
    @field:SerializedName("gsm")
    var gsm: String,
    @field:SerializedName("isNotificationAccepted")
    var isNotificationAccepted: Boolean,
) : BaseRequest(), Parcelable