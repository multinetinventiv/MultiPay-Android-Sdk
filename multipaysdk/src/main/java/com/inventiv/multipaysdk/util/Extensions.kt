package com.inventiv.multipaysdk.util

const val CARD_NUMBER_LENGTH = 19
const val CVV_CARD_LENGTH = 3

fun String.isValidCardNumber(): Boolean {
    return if (this.isEmpty()) {
        false
    } else {
        this.length == CARD_NUMBER_LENGTH
    }
}

fun String.isValidCvv(): Boolean {
    return if (this.isEmpty()) {
        false
    } else {
        this.length >= CVV_CARD_LENGTH
    }
}