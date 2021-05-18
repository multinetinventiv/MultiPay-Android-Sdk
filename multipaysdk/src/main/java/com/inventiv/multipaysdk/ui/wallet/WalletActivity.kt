package com.inventiv.multipaysdk.ui.wallet

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.inventiv.multipaysdk.base.BaseContainerActivity
import com.inventiv.multipaysdk.ui.splash.SplashActivity
import com.inventiv.multipaysdk.util.EXTRA_WALLET_OPEN_ADD_CARD

internal class WalletActivity : BaseContainerActivity() {

    companion object {
        fun newIntent(context: Context, openAddCard: Boolean): Intent {
            return Intent(context, WalletActivity::class.java).apply {
                putExtra(EXTRA_WALLET_OPEN_ADD_CARD, openAddCard)
            }
        }
    }

    override fun fragment(): Fragment {
        return WalletFragment.newInstance(
            (intent?.getBooleanExtra(
                EXTRA_WALLET_OPEN_ADD_CARD,
                false
            ) == true)
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(SplashActivity.newIntent(this@WalletActivity, isCancelled = true))
    }
}