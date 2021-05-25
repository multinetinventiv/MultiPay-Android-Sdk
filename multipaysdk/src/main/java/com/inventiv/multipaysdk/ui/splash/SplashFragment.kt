package com.inventiv.multipaysdk.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inventiv.multipaysdk.base.BaseFragment
import com.inventiv.multipaysdk.data.model.singleton.MultiPayUser
import com.inventiv.multipaysdk.databinding.FragmentSplashMultipaySdkBinding
import com.inventiv.multipaysdk.ui.authentication.AuthenticationActivity
import com.inventiv.multipaysdk.ui.wallet.WalletActivity
import com.inventiv.multipaysdk.util.SPLASH_ACTVITY_REQUEST_CODE

internal class SplashFragment : BaseFragment<FragmentSplashMultipaySdkBinding>() {

    companion object {
        fun newInstance(): SplashFragment = SplashFragment()
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSplashMultipaySdkBinding =
        FragmentSplashMultipaySdkBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (MultiPayUser.walletToken.isNullOrEmpty()) {
            requireActivity().startActivityForResult(
                AuthenticationActivity.startLogin(
                    requireActivity()
                ), SPLASH_ACTVITY_REQUEST_CODE
            )
        } else {
            requireActivity().startActivityForResult(
                WalletActivity.newIntent(
                    requireActivity(),
                    false
                ), SPLASH_ACTVITY_REQUEST_CODE
            )
        }
    }
}