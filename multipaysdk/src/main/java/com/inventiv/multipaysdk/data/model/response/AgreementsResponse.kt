package com.inventiv.multipaysdk.data.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class AgreementsResponse(
    @field:SerializedName("userAgreementUrl")
    var userAgreementUrl: String,
    @field:SerializedName("gdprUrl")
    var gdprUrl: String,
) : Parcelable, BaseResponse