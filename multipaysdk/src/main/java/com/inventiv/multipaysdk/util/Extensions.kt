package com.inventiv.multipaysdk.util

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText

const val CARD_NUMBER_LENGTH = 19
const val CVV_CARD_LENGTH = 3

fun TextInputEditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

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