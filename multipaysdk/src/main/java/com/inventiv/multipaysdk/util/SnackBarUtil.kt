package com.inventiv.multipaysdk.util

import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.inventiv.multipaysdk.R

internal object SnackBarUtil {
    private const val red = -0x16c1c3
    private const val green = -0x8140ae
    private const val yellow = -0x12fd0
    private const val maxLines = 7
    private const val snackbarTextSizeBig = 20f
    private const val snackbarTextSizeMedium = 16f
    private const val snackbarTextSizeSmall = 12f

    private fun getSnackBarLayout(snackbar: Snackbar?): View? {
        return snackbar?.view
    }

    private fun colorSnackBar(snackbar: Snackbar, colorId: Int): Snackbar {
        val snackBarView = getSnackBarLayout(snackbar)
        snackBarView?.setBackgroundColor(colorId)
        snackbar.setActionTextColor(Color.WHITE)
        val view = snackbar.view
        val tv = view.findViewById<TextView>(R.id.snackbar_text)
        tv.setTextColor(Color.WHITE)
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, snackbarTextSizeMedium)
        tv.maxLines = maxLines
        return snackbar
    }

    fun info(snackbar: Snackbar): Snackbar {
        return colorSnackBar(snackbar, yellow)
    }

    fun alert(snackbar: Snackbar): Snackbar {
        return colorSnackBar(snackbar, red)
    }

    fun confirm(snackbar: Snackbar): Snackbar {
        return colorSnackBar(snackbar, green)
    }
}