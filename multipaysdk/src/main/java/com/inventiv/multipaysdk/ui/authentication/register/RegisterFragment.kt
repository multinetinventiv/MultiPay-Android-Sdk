package com.inventiv.multipaysdk.ui.authentication.register

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.checkbox.MaterialCheckBox
import com.inventiv.multipaysdk.MultiPaySdk
import com.inventiv.multipaysdk.R
import com.inventiv.multipaysdk.base.BaseFragment
import com.inventiv.multipaysdk.data.model.EventObserver
import com.inventiv.multipaysdk.data.model.Resource
import com.inventiv.multipaysdk.data.model.request.RegisterRequest
import com.inventiv.multipaysdk.data.model.singleton.MultiPayUser
import com.inventiv.multipaysdk.data.model.type.OtpDirectionFrom
import com.inventiv.multipaysdk.databinding.FragmentRegisterMultipaySdkBinding
import com.inventiv.multipaysdk.repository.AuthenticationRepository
import com.inventiv.multipaysdk.ui.authentication.contract.ContractActivity
import com.inventiv.multipaysdk.ui.authentication.otp.OtpFragment
import com.inventiv.multipaysdk.ui.authentication.otp.OtpNavigationArgs
import com.inventiv.multipaysdk.util.*
import com.inventiv.multipaysdk.util.Validator.NAME_INPUT_MIN_LENGTH
import com.inventiv.multipaysdk.view.listener.PhoneNumberTextWatcher
import com.inventiv.multipaysdk.view.listener.SimpleTextWatcher


internal class RegisterFragment : BaseFragment<FragmentRegisterMultipaySdkBinding>() {

    companion object {

        const val REQUEST_CODE_USER_AGREEMENT = 101
        const val REQUEST_CODE_GDPR = 100

        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewModel: RegisterViewModel
    private lateinit var maskWatcher: PhoneNumberTextWatcher

    private var registerRequest: RegisterRequest? = null

    override fun onResume() {
        super.onResume()
        showToolbar()
        toolbarBack()
        title(R.string.register_header_multipay_sdk)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentRegisterMultipaySdkBinding =
        FragmentRegisterMultipaySdkBinding.inflate(inflater, container, false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModelFactory = RegisterViewModelFactory(
            AuthenticationRepository(
                MultiPaySdk.getComponent().apiService()
            )
        )

        viewModel = ViewModelProvider(
            this@RegisterFragment,
            viewModelFactory
        ).get(RegisterViewModel::class.java)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar().setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        subscribeAgreements()
        viewModel.agreements()
        subscribeRegister()
        registerTextChangeListeners()

        requireBinding().buttonRegisterMultipaySdk.setOnClickListener {
            requireBinding().apply {
                val name = textInputEditNameMultipaySdk.text.toString().trim()
                val surname = textInputEditSurnameMultipaySdk.text.toString().trim()
                val email = textInputEditEmailMultipaySdk.text.toString().trim()
                val gsm = textInputEditGsmMultipaySdk.text.toString().trim()
                val gsmForService = Formatter.servicePhoneNumber(gsm)
                val isNotificationAccepted = checkboxNotificationMultipaySdk.isChecked
                registerRequest =
                    RegisterRequest(name, surname, email, gsmForService, isNotificationAccepted)
                viewModel.register(registerRequest!!)
            }

        }
        validate()
        populateUserPreset()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_USER_AGREEMENT -> {
                if (resultCode == Activity.RESULT_OK) {
                    requireBinding().checkboxUserAgreementMultipaySdk.isChecked = true
                }
            }
            REQUEST_CODE_GDPR -> {
                if (resultCode == Activity.RESULT_OK) {
                    requireBinding().checkboxGdprMultipaySdk.isChecked = true
                }
            }
        }
    }

    private fun subscribeAgreements() {
        viewModel.agreementsResult.observe(viewLifecycleOwner, EventObserver { resource ->
            when (resource) {
                is Resource.Loading -> {
                    setLayoutProgressVisibility(View.VISIBLE)
                }
                is Resource.Success -> {
                    setLayoutProgressVisibility(View.GONE)

                    requireBinding().checkboxUserAgreementMultipaySdk.isClickable = true
                    requireBinding().checkboxGdprMultipaySdk.isClickable = true

                    resource.data?.let {
                        checkboxClicked(
                            REQUEST_CODE_USER_AGREEMENT,
                            requireBinding().checkboxUserAgreementMultipaySdk,
                            getString(R.string.register_contract_multipay_sdk),
                            it.userAgreementUrl
                        )
                        checkboxClicked(
                            REQUEST_CODE_GDPR,
                            requireBinding().checkboxGdprMultipaySdk,
                            getString(R.string.register_kvkk_header_label_multipay_sdk),
                            it.gdprUrl
                        )
                    }
                }
                is Resource.Failure -> {
                    showSnackBarAlert(resource.error.message)
                    setLayoutProgressVisibility(View.GONE)
                }
            }
        })
    }

    private fun subscribeRegister() {
        viewModel.registerResult.observe(viewLifecycleOwner, EventObserver { resource ->
            when (resource) {
                is Resource.Loading -> {
                    setLayoutProgressVisibility(View.VISIBLE)
                }
                is Resource.Success -> {
                    resource.data?.apply {
                        val otpFragment = OtpFragment.newInstance(
                            emailOrGsm = gsm!!,
                            otpNavigationArgs = OtpNavigationArgs(
                                verificationCode,
                                gsm,
                                remainingTime
                            ),
                            registerRequest = registerRequest!!,
                            otpDirectionFrom = OtpDirectionFrom.REGISTER
                        )
                        replaceFragment(otpFragment, R.id.layout_container_multipay_sdk)
                        setLayoutProgressVisibility(View.GONE)
                    }
                }
                is Resource.Failure -> {
                    showSnackBarAlert(resource.error.message)
                    setLayoutProgressVisibility(View.GONE)
                }
            }
        })
    }

    private fun populateUserPreset() {
        if (MultiPayUser.userPreset != null) {
            requireBinding().textInputEditNameMultipaySdk.setText(MultiPayUser.userPreset?.name)
            requireBinding().textInputEditSurnameMultipaySdk.setText(MultiPayUser.userPreset?.surname)
            requireBinding().textInputEditEmailMultipaySdk.setText(MultiPayUser.userPreset?.email)
            if (!MultiPayUser.userPreset?.gsm.isNullOrEmpty()) {
                requireBinding().textInputEditGsmMultipaySdk.setText(MultiPayUser.userPreset?.gsm)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun checkboxClicked(
        requestCode: Int,
        checkBox: MaterialCheckBox,
        title: String,
        url: String,
        buttonVisibility: Boolean = true
    ) {
        checkBox.setOnTouchListener { _, motionEvent ->
            if (checkBox.isChecked) {
                return@setOnTouchListener false
            }

            if (motionEvent.action == MotionEvent.ACTION_UP) {
                startActivityForResult(
                    ContractActivity.newIntent(
                        requireContext(),
                        title,
                        url,
                        buttonVisibility
                    ), requestCode
                )
            }
            return@setOnTouchListener true
        }
    }

    private fun registerTextChangeListeners() {
        requireBinding().textInputEditNameMultipaySdk.onTextChanged { editText, text ->
            editText.checkSpaces(text)
        }
        requireBinding().textInputEditSurnameMultipaySdk.onTextChanged { editText, text ->
            editText.checkSpaces(text)
        }
        requireBinding().textInputEditNameMultipaySdk.afterTextChanged { validate() }
        requireBinding().textInputEditSurnameMultipaySdk.afterTextChanged { validate() }
        requireBinding().textInputEditEmailMultipaySdk.afterTextChanged { validate() }
        requireBinding().textInputEditGsmMultipaySdk.afterTextChanged { validate() }
        requireBinding().textInputEditEmailMultipaySdk.afterTextChanged { validate() }
        requireBinding().checkboxGdprMultipaySdk.setOnCheckedChangeListener { _, _ -> validate() }
        requireBinding().checkboxUserAgreementMultipaySdk.setOnCheckedChangeListener { _, _ -> validate() }

        maskWatcher = PhoneNumberTextWatcher(
            requireBinding().textInputEditGsmMultipaySdk,
            object : SimpleTextWatcher {
            }
        )
        requireBinding().textInputEditGsmMultipaySdk.addTextChangedListener(maskWatcher)
    }

    private fun validate() {
        val validName = requireBinding().textInputEditNameMultipaySdk.text.toString()
            .trim().length >= NAME_INPUT_MIN_LENGTH
        val validSurname = requireBinding().textInputEditSurnameMultipaySdk.text.toString()
            .trim().length >= NAME_INPUT_MIN_LENGTH
        val validPhone =
            Validator.validGsmWithMask(requireBinding().textInputEditGsmMultipaySdk.text.toString())
        val validEmail =
            Validator.validEmail(requireBinding().textInputEditEmailMultipaySdk.text.toString())

        val isValid = (validName && validSurname && validPhone && validEmail
                && requireBinding().checkboxGdprMultipaySdk.isChecked
                && requireBinding().checkboxUserAgreementMultipaySdk.isChecked)

        if (isValid) {
            requireBinding().buttonRegisterMultipaySdk.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.keppel_multipay_sdk
                )
            )
            requireBinding().buttonRegisterMultipaySdk.isClickable = true
        } else {
            requireBinding().buttonRegisterMultipaySdk.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.shadow_green_multipay_sdk
                )
            )
            requireBinding().buttonRegisterMultipaySdk.isClickable = false
        }
    }

    private fun setLayoutProgressVisibility(visibility: Int) {
        requireBinding().registerProgressMultipaySdk.layoutProgressMultipaySdk.visibility =
            visibility
    }
}