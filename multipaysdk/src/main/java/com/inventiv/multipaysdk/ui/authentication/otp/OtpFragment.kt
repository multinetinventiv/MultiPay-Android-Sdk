package com.inventiv.multipaysdk.ui.authentication.otp

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.inventiv.multipaysdk.MultiPaySdk
import com.inventiv.multipaysdk.R
import com.inventiv.multipaysdk.base.BaseFragment
import com.inventiv.multipaysdk.data.model.EventObserver
import com.inventiv.multipaysdk.data.model.Resource
import com.inventiv.multipaysdk.data.model.request.RegisterRequest
import com.inventiv.multipaysdk.data.model.type.OtpDirectionFrom
import com.inventiv.multipaysdk.databinding.FragmentOtpMultipaySdkBinding
import com.inventiv.multipaysdk.repository.AuthenticationRepository
import com.inventiv.multipaysdk.repository.OtpRepository
import com.inventiv.multipaysdk.ui.wallet.WalletActivity
import com.inventiv.multipaysdk.util.*
import com.inventiv.multipaysdk.view.listener.SimpleTextWatcher
import java.util.concurrent.TimeUnit

internal class OtpFragment : BaseFragment<FragmentOtpMultipaySdkBinding>() {

    private var emailOrGsm: String? = null
    private var registerRequest: RegisterRequest? = null
    private var otpNavigationArgs: OtpNavigationArgs? = null
    private var otpDirectionFrom: OtpDirectionFrom? = null

    private lateinit var countDownTimer: CountDownTimer

    private val apiService = MultiPaySdk.getComponent().apiService()

    private lateinit var viewModel: OtpViewModel

    companion object {
        fun newInstance(
            emailOrGsm: String,
            otpNavigationArgs: OtpNavigationArgs,
            registerRequest: RegisterRequest? = null,
            otpDirectionFrom: OtpDirectionFrom
        ): OtpFragment =
            OtpFragment().apply {
                val args = Bundle().apply {
                    putString(ARG_EMAIL_OR_GSM, emailOrGsm)
                    putParcelable(ARG_OTP_NAVIGATION, otpNavigationArgs)
                    putParcelable(ARG_OTP_DIRECTION_FROM, otpDirectionFrom)
                    putParcelable(ARG_OTP_REGISTER_MODEL, registerRequest)
                }
                arguments = args
            }
    }

    private var simpleTextWatcher: TextWatcher = object : SimpleTextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val otpCode = s.toString()
            val otpLength = otpCode.length
            if (otpLength == resources.getInteger(R.integer.otp_length_multipay_sdk)) {
                viewModel.confirmOtp(otpNavigationArgs?.verificationCode, otpCode)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showToolbar()
        toolbarBack()
        title(R.string.otp_navigation_title_multipay_sdk)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentOtpMultipaySdkBinding =
        FragmentOtpMultipaySdkBinding.inflate(inflater, container, false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModelFactory = OtpViewModelFactory(
            OtpRepository(apiService),
            AuthenticationRepository(apiService)
        )
        viewModel =
            ViewModelProvider(this@OtpFragment, viewModelFactory).get(OtpViewModel::class.java)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeConfirmOtp()
        subscribeResendOtp()
        subscribeResendOtpFromRegister()
        emailOrGsm = arguments?.getString(ARG_EMAIL_OR_GSM)
        otpNavigationArgs = arguments?.getParcelable(ARG_OTP_NAVIGATION)
        otpDirectionFrom = arguments?.getParcelable(ARG_OTP_DIRECTION_FROM)
        registerRequest = arguments?.getParcelable(ARG_OTP_REGISTER_MODEL)
        requireBinding().viewPinMultipaySdk.addTextChangedListener(simpleTextWatcher)
        toolbar().setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        val textOtpDescription = getString(
            R.string.otp_description_multipay_sdk,
            Formatter.formatPhoneNumber(otpNavigationArgs?.gsmNumber, true)
        )
        requireBinding().textTitleMultipaySdk.text =
            HtmlCompat.fromHtml(textOtpDescription, HtmlCompat.FROM_HTML_MODE_LEGACY)
        requireBinding().viewPinMultipaySdk.showKeyboard()
        setupAndStartCountDownTimer()
        requireBinding().buttonResendMultipaySdk.setOnClickListener {
            when (otpDirectionFrom) {
                OtpDirectionFrom.LOGIN -> {
                    viewModel.login(emailOrGsm!!)
                }
                OtpDirectionFrom.REGISTER -> {
                    viewModel.register(registerRequest!!)
                }
            }
            requireBinding().viewPinMultipaySdk.setText("")
            requireBinding().buttonResendMultipaySdk.visibility = View.GONE
        }
    }

    private fun setupAndStartCountDownTimer() {
        val seconds = otpNavigationArgs?.remainingTime?.toLong() ?: 100L
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
        countDownTimer = object : CountDownTimer(TimeUnit.SECONDS.toMillis(seconds), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val formattedTimerText =
                    String.format(
                        getString(R.string.otp_remaining_time_multipay_sdk),
                        (millisUntilFinished / 1000)
                    )
                requireBinding().textRemainingTimeMultipaySdk.text = formattedTimerText
            }

            override fun onFinish() {
                requireBinding().buttonResendMultipaySdk.visibility = View.VISIBLE
            }
        }
        countDownTimer.start()
    }

    private fun subscribeConfirmOtp() {
        viewModel.confirmOtpResult.observe(viewLifecycleOwner, EventObserver { resource ->
            when (resource) {
                is Resource.Loading -> {
                    setLayoutProgressVisibility(View.VISIBLE)
                }
                is Resource.Success -> {
                    when (otpDirectionFrom) {
                        OtpDirectionFrom.LOGIN -> {
                            startActivity(
                                WalletActivity.newIntent(requireActivity(), false)
                            )
                            requireActivity().finish()
                        }
                        OtpDirectionFrom.REGISTER -> {
                            startActivity(
                                WalletActivity.newIntent(requireActivity(), true)
                            )
                            requireActivity().finish()
                        }
                    }
                    setLayoutProgressVisibility(View.GONE)
                }
                is Resource.Failure -> {
                    showSnackBarAlert(resource.error.message)
                    setLayoutProgressVisibility(View.GONE)
                    if (resource.error.statusCode == SERVICE_GET_OTP_AGAIN) {
                        countDownTimer.cancel()
                        requireBinding().viewPinMultipaySdk.hideKeyboard()
                        requireBinding().buttonResendMultipaySdk.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun subscribeResendOtp() {
        viewModel.loginResult.observe(viewLifecycleOwner, EventObserver { resource ->
            when (resource) {
                is Resource.Loading -> {
                    setLayoutProgressVisibility(View.VISIBLE)
                }
                is Resource.Success -> {
                    val loginResponse = resource.data
                    otpNavigationArgs =
                        OtpNavigationArgs(
                            loginResponse?.verificationCode,
                            loginResponse?.gsm,
                            loginResponse?.remainingTime
                        )
                    setupAndStartCountDownTimer()
                    setLayoutProgressVisibility(View.GONE)
                }
                is Resource.Failure -> {
                    showSnackBarAlert(resource.error.message)
                    setLayoutProgressVisibility(View.GONE)
                }
            }
        })
    }

    private fun subscribeResendOtpFromRegister() {
        viewModel.registerResult.observe(viewLifecycleOwner, EventObserver { resource ->
            when (resource) {
                is Resource.Loading -> {
                    setLayoutProgressVisibility(View.VISIBLE)
                }
                is Resource.Success -> {
                    val registerResponse = resource.data
                    otpNavigationArgs =
                        OtpNavigationArgs(
                            verificationCode = registerResponse?.verificationCode,
                            gsmNumber = registerResponse?.gsm,
                            remainingTime = registerResponse?.remainingTime
                        )
                    setupAndStartCountDownTimer()
                    setLayoutProgressVisibility(View.GONE)
                }
                is Resource.Failure -> {
                    showSnackBarAlert(resource.error.message)
                    setLayoutProgressVisibility(View.GONE)
                }
            }
        })
    }

    private fun setLayoutProgressVisibility(visibility: Int) {
        requireBinding().otpProgressMultipaySdk.layoutProgressMultipaySdk.visibility = visibility
    }

    override fun onDestroyView() {
        requireBinding().viewPinMultipaySdk.removeTextChangedListener(simpleTextWatcher)
        countDownTimer.cancel()
        super.onDestroyView()
    }
}