package com.inventiv.multipaysdk.sample

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentRollback(
    val requestId: String,
    val terminalReferenceNumber: String,
    val merchantReferenceNumber: String,
    val rollbackReferenceNumber: String,
    val reason: Int,
    val referenceNumberType: Int,
    val referenceNumber: String,
    val sign: String
) : Parcelable