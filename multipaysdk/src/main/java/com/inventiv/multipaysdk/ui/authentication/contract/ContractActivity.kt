package com.inventiv.multipaysdk.ui.authentication.contract

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.inventiv.multipaysdk.base.BaseContainerActivity
import com.inventiv.multipaysdk.util.EXTRA_BUTTON_VISIBILITY
import com.inventiv.multipaysdk.util.EXTRA_WEB_TITLE
import com.inventiv.multipaysdk.util.EXTRA_WEB_URL

internal class ContractActivity : BaseContainerActivity() {

    companion object {
        fun newIntent(
            context: Context,
            title: String,
            url: String,
            buttonVisibility: Boolean
        ): Intent {
            return Intent(context, ContractActivity::class.java).apply {
                putExtra(EXTRA_WEB_TITLE, title)
                putExtra(EXTRA_WEB_URL, url)
                putExtra(EXTRA_BUTTON_VISIBILITY, buttonVisibility)
            }
        }
    }

    override fun fragment(): Fragment {
        val webTitle = intent.getStringExtra(EXTRA_WEB_TITLE)
        val webUrl = intent.getStringExtra(EXTRA_WEB_URL)
        val buttonVisibility = intent.getBooleanExtra(EXTRA_BUTTON_VISIBILITY, true)

        return ContractFragment.newInstance(
            webTitle!!,
            webUrl!!,
            buttonVisibility
        )
    }
}