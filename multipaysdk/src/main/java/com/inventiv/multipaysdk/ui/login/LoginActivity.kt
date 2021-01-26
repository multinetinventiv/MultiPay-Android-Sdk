package com.inventiv.multipaysdk.ui.login

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.inventiv.multipaysdk.base.BaseContainerActivity

internal class LoginActivity : BaseContainerActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    override fun fragment(): Fragment = LoginFragment.newInstance()

    override fun onBackPressed() {
        setResult(RESULT_OK)
        finish()
    }
}