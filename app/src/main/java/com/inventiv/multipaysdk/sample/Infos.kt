package com.inventiv.multipaysdk.sample

import android.os.Parcelable
import com.inventiv.multipaysdk.Environment
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Infos(
    val environment: Environment,
    val walletApptoken: String,
    val paymentAppToken: String,
    val userID: String,
    val merchantReferenceNumber: String,
    val terminalReferenceNumber: String,
    val amount: String,
    val productId: String,
    val sign: String
) : Parcelable