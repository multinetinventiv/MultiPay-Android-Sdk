package com.inventiv.multipaysdk.ui.authentication

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.inventiv.multipaysdk.R
import com.inventiv.multipaysdk.base.BaseContainerActivity
import com.inventiv.multipaysdk.data.model.type.AuthenticationType
import com.inventiv.multipaysdk.data.model.type.OtpDirectionFrom
import com.inventiv.multipaysdk.ui.authentication.login.LoginFragment
import com.inventiv.multipaysdk.ui.authentication.otp.OtpFragment
import com.inventiv.multipaysdk.ui.authentication.otp.OtpNavigationArgs
import com.inventiv.multipaysdk.util.*

internal class AuthenticationActivity : BaseContainerActivity() {

    companion object {
        fun startLogin(context: Context): Intent {
            return Intent(context, AuthenticationActivity::class.java).apply {
                putExtra(EXTRA_AUTHENTICATION_TYPE, AuthenticationType.LOGIN)
            }
        }

        fun startOtp(
            context: Context,
            emailOrGsm: String,
            otpNavigationArgs: OtpNavigationArgs,
            otpDirectionFrom: OtpDirectionFrom
        ): Intent {
            return Intent(context, AuthenticationActivity::class.java).apply {
                putExtra(EXTRA_AUTHENTICATION_TYPE, AuthenticationType.OTP)
                putExtra(EXTRA_EMAIL_OR_GSM, emailOrGsm)
                putExtra(EXTRA_OTP_NAVIGATION, otpNavigationArgs)
                putParcelableExtra(EXTRA_OTP_DIRECTION_FROM, otpDirectionFrom)
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
            AuthenticationType.OTP -> {
                val emailOrGsm = intent.getStringExtra(EXTRA_EMAIL_OR_GSM)
                val otpNavigationArgs: OtpNavigationArgs? =
                    intent.getParcelableExtra(EXTRA_OTP_NAVIGATION)
                val otpDirectionFrom: OtpDirectionFrom? =
                    intent.getParcelableExtra(EXTRA_OTP_DIRECTION_FROM)
                OtpFragment.newInstance(
                    emailOrGsm!!,
                    otpNavigationArgs!!,
                    otpDirectionFrom!!
                )
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
        }
    }
}