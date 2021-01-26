package com.inventiv.multipaysdk.data.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class WalletsResponse(
    @field:SerializedName("wallets")
    val wallets: List<WalletResponse>
) : Parcelable, BaseResponse