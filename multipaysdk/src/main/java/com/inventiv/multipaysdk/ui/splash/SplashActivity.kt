package com.inventiv.multipaysdk.ui.splash

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.inventiv.multipaysdk.base.BaseContainerActivity
import com.inventiv.multipaysdk.util.EXTRA_SPLASH
import com.inventiv.multipaysdk.util.SDK_CLOSED
import com.inventiv.multipaysdk.util.SPLASH_ACTVITY_REQUEST_CODE

internal class SplashActivity : BaseContainerActivity() {

    companion object {
        fun newIntent(context: Context, isCancelled: Boolean = false): Intent {
            return Intent(context, SplashActivity::class.java).apply {
                putExtra(EXTRA_SPLASH, isCancelled)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SPLASH_ACTVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                sendBroadcast(Intent(SDK_CLOSED))
                finish()
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val isCancelled = intent?.getBooleanExtra(EXTRA_SPLASH, false)
        if (isCancelled!!) {
            sendBroadcast(Intent(SDK_CLOSED))
            finish()
        }
    }

    override fun fragment(): Fragment = SplashFragment.newInstance()
}