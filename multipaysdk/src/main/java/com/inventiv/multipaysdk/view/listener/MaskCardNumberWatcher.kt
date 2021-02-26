package com.inventiv.multipaysdk.view.listener

import android.text.Editable
import android.text.TextWatcher

internal class MaskCardNumberWatcher(
    private val maskWatcherView: MaskCardNumberWatcherView,
    private var lock: Boolean = false
) : TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        maskWatcherView.beforeTextTriggered()
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        maskWatcherView.onTextTriggered()
    }

    override fun afterTextChanged(s: Editable?) {
        if (lock || s!!.length > 16) {
            maskWatcherView.afterTextTriggered()
            return
        }
        lock = true
        for (i in 4 until s.length step 5) {
            if (s.toString()[i] != ' ') {
                s.insert(i, " ")
            }
        }
        lock = false
        maskWatcherView.afterTextTriggered()
    }
}