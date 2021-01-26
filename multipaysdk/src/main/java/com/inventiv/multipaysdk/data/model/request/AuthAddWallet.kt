package com.inventiv.multipaysdk.data.model.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.inventiv.multipaysdk.MultiPaySdk
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
internal data class AuthAddWallet(
    @field:SerializedName("number")
    var number: String,
    @field:SerializedName("cvv")
    var cvv: String,
    @field:SerializedName("alias")
    var alias: String,
    @field:SerializedName("referenceNumber")
    var referenceNumber: String = MultiPaySdk.getComponent().clientReferenceNo(),
    @field:SerializedName("requestId")
    var requestId: String = UUID.randomUUID().toString()
) : Parcelable, BaseAuthRequest(), AddWalletRequest