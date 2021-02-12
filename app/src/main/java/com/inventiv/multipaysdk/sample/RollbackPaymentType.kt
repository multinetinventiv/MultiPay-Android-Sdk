package com.inventiv.multipaysdk.sample

enum class RollbackPaymentType(val code: Int) {
    CANCEL(2), REVERSAL(3), REFUND(4)
}