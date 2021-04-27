package com.inventiv.multipaysdk.ui.authentication.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.inventiv.multipaysdk.MultiPaySdk
import com.inventiv.multipaysdk.R
import com.inventiv.multipaysdk.base.BaseFragment
import com.inventiv.multipaysdk.data.model.EventObserver
import com.inventiv.multipaysdk.data.model.Resource
import com.inventiv.multipaysdk.data.model.type.OtpDirectionFrom
import com.inventiv.multipaysdk.databinding.FragmentLoginMultipaySdkBinding
import com.inventiv.multipaysdk.repository.AuthenticationRepository
import com.inventiv.multipaysdk.ui.authentication.otp.OtpFragment
import com.inventiv.multipaysdk.ui.authentication.otp.OtpNavigationArgs
import com.inventiv.multipaysdk.util.hideKeyboard
import com.inventiv.multipaysdk.util.replaceFragment
import com.inventiv.multipaysdk.util.showSnackBarAlert
import com.inventiv.multipaysdk.util.showToolbar
import com.inventiv.multipaysdk.view.listener.MaskWatcher


internal class LoginFragment : BaseFragment<FragmentLoginMultipaySdkBinding>() {

    private lateinit var maskWatcher: MaskWatcher
    private lateinit var emailOrGsm: String

    private lateinit var viewModel: LoginViewModel

    companion object {
        fun newInstance(): LoginFragment = LoginFragment()
    }

    override fun onResume() {
        super.onResume()
        showToolbar(false)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentLoginMultipaySdkBinding =
        FragmentLoginMultipaySdkBinding.inflate(inflater, container, false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModelFactory =
            LoginViewModelFactory(AuthenticationRepository(MultiPaySdk.getComponent().apiService()))
        viewModel =
            ViewModelProvider(this@LoginFragment, viewModelFactory).get(LoginViewModel::class.java)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeLogin()
        val maskPhone = getString(R.string.mask_phone_multipay_sdk)
        maskWatcher = MaskWatcher(requireBinding().textInputEditEmailOrGsmMultipaySdk, maskPhone)
        requireBinding().textInputEditEmailOrGsmMultipaySdk.addTextChangedListener(maskWatcher)
        requireBinding().buttonLoginMultipaySdk.setOnClickListener {
            loginClicked()
        }
    }

    override fun onDestroyView() {
        requireBinding().textInputEditEmailOrGsmMultipaySdk.removeTextChangedListener(maskWatcher)
        super.onDestroyView()
    }

    private fun subscribeLogin() {
        viewModel.loginResult.observe(viewLifecycleOwner, EventObserver { resource ->
            when (resource) {
                is Resource.Loading -> {
                    setLayoutProgressVisibility(View.VISIBLE)
                }
                is Resource.Success -> {
                    val loginResponse = resource.data
                    val otpFragment = OtpFragment.newInstance(
                        emailOrGsm,
                        OtpNavigationArgs(
                            loginResponse?.verificationCode,
                            loginResponse?.gsm,
                            loginResponse?.remainingTime
                        ),
                        OtpDirectionFrom.LOGIN
                    )
                    replaceFragment(otpFragment, R.id.layout_container_multipay_sdk)
                    setLayoutProgressVisibility(View.GONE)
                }
                is Resource.Failure -> {
                    showSnackBarAlert(resource.message)
                    setLayoutProgressVisibility(View.GONE)
                }
            }
        })
    }

    private fun setLayoutProgressVisibility(visibility: Int) {
        requireBinding().loginProgressMultipaySdk.layoutProgressMultipaySdk.visibility = visibility
    }

    private fun loginClicked() {
        requireBinding().textInputEditEmailOrGsmMultipaySdk.hideKeyboard()
        emailOrGsm = requireBinding().textInputEditEmailOrGsmMultipaySdk.text.toString().trim()
        viewModel.login(emailOrGsm)
    }
}