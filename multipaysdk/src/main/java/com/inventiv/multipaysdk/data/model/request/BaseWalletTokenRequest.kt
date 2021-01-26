package com.inventiv.multipaysdk.data.model.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.inventiv.multipaysdk.data.model.singleton.MultiPayUser
import kotlinx.android.parcel.Parcelize

@Parcelize
internal open class BaseWalletTokenRequest(
    @field:SerializedName("walletToken")
    var walletToken: String? = MultiPayUser.walletToken
) : Parcelable, BaseRequest()