package com.inventiv.multipaysdk.sample

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.getSharedPref(): SharedPreferences {
    return this.getSharedPreferences("MultiPaySdkSample", Context.MODE_PRIVATE)
}