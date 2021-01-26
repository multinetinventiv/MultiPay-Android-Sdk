package com.inventiv.multipaysdk.ui.addwallet

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.inventiv.multipaysdk.base.BaseContainerActivity

internal class AddWalletActivity : BaseContainerActivity() {

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, AddWalletActivity::class.java)
    }

    override fun fragment(): Fragment = AddWalletFragment.newInstance()
}