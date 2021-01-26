package com.inventiv.multipaysdk.ui.wallet

import android.content.Context
import android.content.Intent
import com.inventiv.multipaysdk.base.BaseContainerActivity
import com.inventiv.multipaysdk.ui.splash.SplashActivity

internal class WalletActivity : BaseContainerActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, WalletActivity::class.java)
        }
    }

    override fun fragment() = WalletFragment.newInstance()

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(SplashActivity.newIntent(this@WalletActivity, isCancelled = true))
    }
}