package com.inventiv.multipaysdk.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.inventiv.multipaysdk.MultiPaySdk
import com.inventiv.multipaysdk.R
import com.inventiv.multipaysdk.base.BaseFragment
import com.inventiv.multipaysdk.data.model.EventObserver
import com.inventiv.multipaysdk.data.model.Resource
import com.inventiv.multipaysdk.data.model.type.OtpDirectionFrom
import com.inventiv.multipaysdk.databinding.FragmentLoginBinding
import com.inventiv.multipaysdk.repository.AuthenticationRepository
import com.inventiv.multipaysdk.ui.otp.OtpActivity
import com.inventiv.multipaysdk.ui.otp.OtpNavigationArgs
import com.inventiv.multipaysdk.util.hideKeyboard
import com.inventiv.multipaysdk.util.showSnackBarAlert
import com.inventiv.multipaysdk.view.listener.MaskWatcher


internal class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private lateinit var maskWatcher: MaskWatcher
    private lateinit var emailOrGsm: String
    private lateinit var password: String

    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(AuthenticationRepository(MultiPaySdk.getComponent().apiService()))
    }

    companion object {
        fun newInstance(): LoginFragment = LoginFragment()
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeLogin()
        val maskPhone = getString(R.string.mask_phone)
        maskWatcher = MaskWatcher(requireBinding().textInputEditEmailOrGsm, maskPhone)
        requireBinding().textInputEditEmailOrGsm.addTextChangedListener(maskWatcher)
        requireBinding().buttonLogin.setOnClickListener {
            loginClicked()
        }
    }

    override fun onDestroyView() {
        requireBinding().textInputEditEmailOrGsm.removeTextChangedListener(maskWatcher)
        super.onDestroyView()
    }

    private fun subscribeLogin() {
        viewModel.loginResult.observe(viewLifecycleOwner, EventObserver { resource ->
            when (resource) {
                is Resource.Loading -> {
                    setLayoutProgressVisibility(View.VISIBLE)
                }
                is Resource.Success -> {
                    requireActivity().finish()
                    val loginResponse = resource.data
                    startActivity(
                        OtpActivity.newIntent(
                            requireActivity(),
                            emailOrGsm,
                            password,
                            OtpNavigationArgs(
                                loginResponse?.verificationCode,
                                loginResponse?.gsm,
                                loginResponse?.remainingTime
                            ),
                            OtpDirectionFrom.LOGIN
                        )
                    )
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
        requireBinding().loginProgress.layoutProgress.visibility = visibility
    }

    private fun loginClicked() {
        requireBinding().textInputEditEmailOrGsm.hideKeyboard()
        requireBinding().textInputEditPassword.hideKeyboard()
        emailOrGsm = requireBinding().textInputEditEmailOrGsm.text.toString().trim()
        password = requireBinding().textInputEditPassword.text.toString().trim()
        viewModel.login(emailOrGsm, password)
    }
}