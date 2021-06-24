package com.inventiv.multipaysdk.ui.authentication

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.inventiv.multipaysdk.R
import com.inventiv.multipaysdk.base.BaseContainerActivity
import com.inventiv.multipaysdk.data.model.type.AuthenticationType
import com.inventiv.multipaysdk.ui.authentication.login.LoginFragment
import com.inventiv.multipaysdk.ui.authentication.otp.OtpFragment
import com.inventiv.multipaysdk.ui.authentication.register.RegisterFragment
import com.inventiv.multipaysdk.util.EXTRA_AUTHENTICATION_TYPE

internal class AuthenticationActivity : BaseContainerActivity() {

    companion object {
        fun startLogin(context: Context): Intent {
            return Intent(context, AuthenticationActivity::class.java).apply {
                putExtra(EXTRA_AUTHENTICATION_TYPE, AuthenticationType.LOGIN)
            }
        }
    }

    override fun fragment(): Fragment {
        val authenticationType =
            intent.getSerializableExtra(EXTRA_AUTHENTICATION_TYPE) as AuthenticationType

        return when (authenticationType) {
            AuthenticationType.LOGIN -> {
                LoginFragment.newInstance()
            }
            else -> {
                return Fragment()
            }
        }
    }

    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(R.id.layout_container_multipay_sdk)) {
            is LoginFragment -> {
                setResult(RESULT_OK)
                finish()
            }
            is OtpFragment -> {
                super.onBackPressed()
            }
            is RegisterFragment -> {
                super.onBackPressed()
            }
        }
    }
}