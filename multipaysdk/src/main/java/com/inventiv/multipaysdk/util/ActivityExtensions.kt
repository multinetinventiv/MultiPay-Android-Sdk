package com.inventiv.multipaysdk.util

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

internal fun FragmentActivity.addFragment(fragment: Fragment, frameId: Int) {
    val fragmentTransaction = supportFragmentManager.beginTransaction()
    fragmentTransaction.add(frameId, fragment, fragment.javaClass.simpleName)
    fragmentTransaction.commit()
}

internal fun Intent.putParcelableExtra(name: String, value: Parcelable) {
    putExtra(name, value)
}

internal fun Context.themeColor(@AttrRes attrRes: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrRes, typedValue, true)
    return typedValue.data
}