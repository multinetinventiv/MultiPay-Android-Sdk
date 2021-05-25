package com.inventiv.multipaysdk.util

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputEditText

internal fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

internal fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

fun AppCompatEditText.checkSpaces(text: String) {
    val preValue: String = this.text.toString()
    // To avoid empty space as a first character
    if (preValue.isNotEmpty() && preValue.trim().isEmpty()) {
        this.setText(preValue.substring(0, text.length - 1))
        this.setSelection(this.text.toString().length)
    }
    // Restrict EditText to allow only one space after a character
    if (preValue.endsWith(DOUBLE_SPACE)) {
        this.setText(preValue.substring(0, text.length - 1))
        this.setSelection(this.text.toString().length)
    }
}

fun TextInputEditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}


fun AppCompatEditText.onTextChanged(onTextChanged: (AppCompatEditText, String) -> Unit) {
    val editText = this
    editText.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged.invoke(editText, s.toString())
        }
    })
}