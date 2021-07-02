package com.inventiv.multipaysdk.sample

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentInfos(
    val requestId: String,
    val transferReferenceNumber: String,
    val terminalReferenceNumber: String,
    val merchantReferenceNumber: String,
    val amount: String,
    val productId: String,
    val referenceNumber: String,
    val sign: String,
    var transferServerRefNo: String? = null,
    var reason: Int? = null
) : Parcelable