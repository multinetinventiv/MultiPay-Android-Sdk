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
import com.inventiv.multipaysdk.data.model.type.OtpDirectionFrom
import com.inventiv.multipaysdk.databinding.FragmentOtpBinding
import com.inventiv.multipaysdk.repository.AuthenticationRepository
import com.inventiv.multipaysdk.repository.OtpRepository
import com.inventiv.multipaysdk.ui.wallet.WalletActivity
import com.inventiv.multipaysdk.util.*
import com.inventiv.multipaysdk.view.listener.SimpleTextWatcher
import java.util.concurrent.TimeUnit

internal class OtpFragment : BaseFragment<FragmentOtpBinding>() {

    private var emailOrGsm: String? = null
    private var password: String? = null
    private var otpNavigationArgs: OtpNavigationArgs? = null
    private var otpDirectionFrom: OtpDirectionFrom? = null

    private lateinit var countDownTimer: CountDownTimer

    private val apiService = MultiPaySdk.getComponent().apiService()

    private lateinit var viewModel: OtpViewModel

    companion object {
        fun newInstance(
            emailOrGsm: String,
            password: String,
            otpNavigationArgs: OtpNavigationArgs,
            otpDirectionFrom: OtpDirectionFrom
        ): OtpFragment =
            OtpFragment().apply {
                val args = Bundle().apply {
                    putString(ARG_EMAIL_OR_GSM, emailOrGsm)
                    putString(ARG_PASSWORD, password)
                    putParcelable(ARG_OTP_NAVIGATION, otpNavigationArgs)
                    putParcelable(ARG_OTP_DIRECTION_FROM, otpDirectionFrom)
                }
                arguments = args
            }
    }

    private var simpleTextWatcher: TextWatcher = object : SimpleTextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val otpCode = s.toString()
            val otpLength = otpCode.length
            if (otpLength == resources.getInteger(R.integer.otp_length)) {
                viewModel.confirmOtp(otpNavigationArgs?.verificationCode, otpCode)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showToolbar()
        toolbarBack()
        title(R.string.otp_navigation_title)

    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentOtpBinding = FragmentOtpBinding.inflate(inflater, container, false)

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
        emailOrGsm = arguments?.getString(ARG_EMAIL_OR_GSM)
        password = arguments?.getString(ARG_PASSWORD)
        otpNavigationArgs = arguments?.getParcelable(ARG_OTP_NAVIGATION)
        otpDirectionFrom = arguments?.getParcelable(ARG_OTP_DIRECTION_FROM)
        requireBinding().viewPin.addTextChangedListener(simpleTextWatcher)
        toolbar().setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        val textOtpDescription = getString(
            R.string.otp_description,
            Formatter.formatPhoneNumber(otpNavigationArgs?.gsmNumber, true)
        )
        requireBinding().textTitle.text =
            HtmlCompat.fromHtml(textOtpDescription, HtmlCompat.FROM_HTML_MODE_LEGACY)
        requireBinding().viewPin.showKeyboard()
        setupAndStartCountDownTimer()
        requireBinding().buttonResend.setOnClickListener {
            viewModel.login(emailOrGsm!!, password!!)
            requireBinding().buttonResend.visibility = View.GONE
        }
    }

    private fun setupAndStartCountDownTimer() {
        val seconds = otpNavigationArgs?.remainingTime?.toLong() ?: 100L
        countDownTimer = object : CountDownTimer(TimeUnit.SECONDS.toMillis(seconds), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val formattedTimerText =
                    String.format(
                        getString(R.string.otp_remaining_time),
                        (millisUntilFinished / 1000)
                    )
                requireBinding().textRemainingTime.text = formattedTimerText
            }

            override fun onFinish() {
                requireBinding().buttonResend.visibility = View.VISIBLE
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
                            requireActivity().finish()
                            startActivity(
                                WalletActivity.newIntent(requireActivity())
                            )
                        }
                        OtpDirectionFrom.CREATE_CARD -> {
                            // TODO : manage navigation
                        }
                    }
                    setLayoutProgressVisibility(View.GONE)
                }
                is Resource.Failure -> {
                    showSnackBarAlert(resource.message)
                    setLayoutProgressVisibility(View.GONE)
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
                    showSnackBarAlert(resource.message)
                    setLayoutProgressVisibility(View.GONE)
                }
            }
        })
    }

    private fun setLayoutProgressVisibility(visibility: Int) {
        requireBinding().otpProgress.layoutProgress.visibility = visibility
    }

    override fun onDestroyView() {
        requireBinding().viewPin.removeTextChangedListener(simpleTextWatcher)
        countDownTimer.cancel()
        super.onDestroyView()
    }
}