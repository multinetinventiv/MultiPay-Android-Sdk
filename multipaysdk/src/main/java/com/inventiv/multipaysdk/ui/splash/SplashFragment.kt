package com.inventiv.multipaysdk.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inventiv.multipaysdk.base.BaseFragment
import com.inventiv.multipaysdk.data.model.singleton.MultiPayUser
import com.inventiv.multipaysdk.databinding.FragmentSplashBinding
import com.inventiv.multipaysdk.ui.login.LoginActivity
import com.inventiv.multipaysdk.ui.wallet.WalletActivity
import com.inventiv.multipaysdk.util.SPLASH_ACTVITY_REQUEST_CODE

internal class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    companion object {
        fun newInstance(): SplashFragment = SplashFragment()
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSplashBinding = FragmentSplashBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (MultiPayUser.walletToken.isNullOrEmpty()) {
            requireActivity().startActivityForResult(
                LoginActivity.newIntent(
                    requireActivity()
                ), SPLASH_ACTVITY_REQUEST_CODE
            )
        } else {
            requireActivity().startActivityForResult(
                WalletActivity.newIntent(
                    requireActivity()
                ), SPLASH_ACTVITY_REQUEST_CODE
            )
        }
    }
}