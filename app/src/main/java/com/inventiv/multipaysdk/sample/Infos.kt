package com.inventiv.multipaysdk.sample

import android.os.Parcelable
import com.inventiv.multipaysdk.Environment
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Infos(
    val environment: Environment,
    val walletApptoken: String,
    val paymentAppToken: String,
    val saltKey: String,
    val userID: String
) : Parcelable