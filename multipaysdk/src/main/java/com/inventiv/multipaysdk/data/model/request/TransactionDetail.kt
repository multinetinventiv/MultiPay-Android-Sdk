package com.inventiv.multipaysdk.data.model.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionDetail(
    @field:SerializedName("amount")
    val amount: String,
    @field:SerializedName("productId")
    val productId: String,
    @field:SerializedName("referenceNumber")
    val referenceNumber: String
) : Parcelable