package com.inventiv.multipaysdk.util

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.inventiv.multipaysdk.R

fun Fragment.toolbar() = requireActivity().findViewById<Toolbar>(R.id.toolbar_multipay_sdk)!!

fun Fragment.title(strId: Int) {
    toolbar().title = context?.getString(strId)
}

fun Fragment.title(text: String) {
    toolbar().title = text
}

fun Fragment.toolbarBackground(resId: Int) {
    val drawable = ContextCompat.getDrawable(requireContext(), resId)
    toolbar().background = drawable
}

fun Fragment.toolbarColor(colorId: Int) {
    toolbar().setBackgroundColor(colorId)
}

fun Fragment.toolbarClose() {
    toolbarIcon(R.drawable.ic_nav_close_multipay_sdk)
}

fun Fragment.hideToolbarBack() {
    toolbar().navigationIcon = null
}

fun Fragment.toolbarBack() {
    toolbarIcon(R.drawable.ic_nav_back_multipay_sdk)
}

fun Fragment.toolbarIcon(drawableId: Int) {
    val drawable = ContextCompat.getDrawable(requireContext(), drawableId)
    toolbar().navigationIcon = drawable
}

fun Fragment.showToolbar(isVisible: Boolean = true) {
    toolbar().visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

