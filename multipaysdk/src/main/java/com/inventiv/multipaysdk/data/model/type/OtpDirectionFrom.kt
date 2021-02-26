package com.inventiv.multipaysdk.data.model.type

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
internal enum class OtpDirectionFrom : Parcelable {
    LOGIN, CREATE_CARD
}