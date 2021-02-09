package com.inventiv.multipaysdk.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.inventiv.multipaysdk.MultiPaySdk
import com.inventiv.multipaysdk.R
import com.inventiv.multipaysdk.databinding.ActivityCommonMultipaySdkBinding
import com.inventiv.multipaysdk.util.addFragment
import com.inventiv.multipaysdk.util.updateBaseContextLocale

internal abstract class BaseContainerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommonMultipaySdkBinding

    override fun attachBaseContext(newBase: Context?) {
        if (newBase == null) {
            super.attachBaseContext(newBase)
        } else {
            super.attachBaseContext(
                updateBaseContextLocale(
                    newBase,
                    MultiPaySdk.getComponent().language().id
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommonMultipaySdkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val fragment = supportFragmentManager.findFragmentById(R.id.layout_container_multipay_sdk)
        if (fragment == null) {
            addFragment(fragment(), R.id.layout_container_multipay_sdk)
        }
        binding.toolbarMultipaySdk.setNavigationOnClickListener {
            finish()
        }
    }

    protected abstract fun fragment(): Fragment

    fun requireBinding() = binding
}