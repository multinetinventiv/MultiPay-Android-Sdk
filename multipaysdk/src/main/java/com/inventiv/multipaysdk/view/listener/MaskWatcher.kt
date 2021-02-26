package com.inventiv.multipaysdk.view.listener

import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.widget.EditText
import java.util.*

internal class MaskWatcher(editText: EditText, mask: String) : TextWatcher {
    private var isSpecialCharsReplaced = false
    private var isRunning = false
    private var isDeleting = false
    private val mask: String
    private val editText: EditText
    private val defaultFilter: Array<InputFilter>
    private val maskFilter: Array<InputFilter>

    companion object {
        const val MASK_CHAR = '#'
    }

    init {
        val res = editText.context.resources
        this.mask = mask
        this.editText = editText
        val defaultLength = 200
        defaultFilter = arrayOf(LengthFilter(defaultLength))
        maskFilter = arrayOf(LengthFilter(mask.length))
    }

    override fun beforeTextChanged(
        charSequence: CharSequence,
        start: Int,
        count: Int,
        after: Int
    ) {
        isDeleting = count > after
    }

    override fun onTextChanged(
        charSequence: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
    }

    override fun afterTextChanged(editable: Editable) {
        if (isRunning || isDeleting) {
            return
        }
        isRunning = true
        if (hasAlpha(editable.toString())) {
            editText.filters = defaultFilter
            if (!isSpecialCharsReplaced) {
                val positions = specialCharPositions
                for (position in positions) {
                    val c = mask[position!!]
                    val pos = editable.toString().indexOf(c)
                    if (pos != -1) {
                        editable.replace(pos, pos + 1, "")
                    }
                }
                isSpecialCharsReplaced = true
            }
        } else {
            isSpecialCharsReplaced = false
            editText.filters = maskFilter
            val editableLength = editable.length
            if (editableLength < mask.length && editableLength != 0) {
                if (mask[editableLength - 1] != MASK_CHAR) {
                    val unmaskedChars =
                        getSequentialUnmaskedChars(editableLength - 1)
                    val lastUnMaskedChars = unmaskedChars[unmaskedChars.length - 1]
                    if (lastUnMaskedChars == editable[editableLength - 1]) {
                        val length = unmaskedChars.length
                        val unmaskedCharWithoutLast =
                            unmaskedChars.substring(0, length - 1)
                        editable.insert(editableLength - 1, unmaskedCharWithoutLast)
                    } else {
                        editable.insert(editableLength - 1, unmaskedChars)
                    }
                } else if (mask[editableLength] != MASK_CHAR) {
                    editable.append(mask[editableLength])
                }
            }
        }
        isRunning = false
    }

    private fun getSequentialUnmaskedChars(position: Int): String {
        var str = ""
        for (i in position until mask.length) {
            val c = mask[i]
            str += if (c != MASK_CHAR) {
                c
            } else {
                break
            }
        }
        return str
    }

    private fun hasAlpha(str: String): Boolean {
        return str.matches(".*[a-zA-Z]+.*".toRegex())
    }

    private val specialCharPositions: ArrayList<Int?>
        get() {
            val positions = ArrayList<Int?>()
            for (i in mask.indices) {
                if (mask[i] != '#') {
                    positions.add(i)
                }
            }
            return positions
        }
}