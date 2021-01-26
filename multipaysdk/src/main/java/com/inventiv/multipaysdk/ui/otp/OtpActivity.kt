package com.inventiv.multipaysdk.ui.otp

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.inventiv.multipaysdk.base.BaseContainerActivity
import com.inventiv.multipaysdk.data.model.type.OtpDirectionFrom
import com.inventiv.multipaysdk.util.*

internal class OtpActivity : BaseContainerActivity() {

    companion object {
        fun newIntent(
            context: Context,
            emailOrGsm: String,
            password: String,
            otpNavigationArgs: OtpNavigationArgs,
            otpDirectionFrom: OtpDirectionFrom
        ): Intent {
            return Intent(context, OtpActivity::class.java).apply {
                putExtra(EXTRA_EMAIL_OR_GSM, emailOrGsm)
                putExtra(EXTRA_PASSWORD, password)
                putExtra(EXTRA_OTP_NAVIGATION, otpNavigationArgs)
                putParcelableExtra(EXTRA_OTP_DIRECTION_FROM, otpDirectionFrom)
            }
        }
    }

    private var emailOrGsm: String? = null
    private var password: String? = null
    private var otpNavigationArgs: OtpNavigationArgs? = null
    private var otpDirectionFrom: OtpDirectionFrom? = null

    override fun fragment(): Fragment {
        emailOrGsm = intent.getStringExtra(EXTRA_EMAIL_OR_GSM)
        password = intent.getStringExtra(EXTRA_PASSWORD)
        otpNavigationArgs = intent.getParcelableExtra(EXTRA_OTP_NAVIGATION)
        otpDirectionFrom = intent.getParcelableExtra(EXTRA_OTP_DIRECTION_FROM)
        return OtpFragment.newInstance(
            emailOrGsm!!,
            password!!,
            otpNavigationArgs!!,
            otpDirectionFrom!!
        )
    }
}