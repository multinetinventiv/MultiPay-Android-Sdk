package com.inventiv.multipaysdk.ui.authentication.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.inventiv.multipaysdk.MultiPaySdk
import com.inventiv.multipaysdk.R
import com.inventiv.multipaysdk.base.BaseFragment
import com.inventiv.multipaysdk.databinding.FragmentRegisterMultipaySdkBinding
import com.inventiv.multipaysdk.repository.AuthenticationRepository
import com.inventiv.multipaysdk.util.*
import com.inventiv.multipaysdk.view.listener.PhoneNumberTextWatcher
import com.inventiv.multipaysdk.view.listener.SimpleTextWatcher


internal class RegisterFragment : BaseFragment<FragmentRegisterMultipaySdkBinding>() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewModel: RegisterViewModel
    private lateinit var maskWatcher: PhoneNumberTextWatcher

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

        registerTextChangeListeners()

    }

    private fun registerTextChangeListeners() {
        requireBinding().textInputEditNameMultipaySdk.onTextChanged { editText, text ->
            editText.checkSpaces(text)
        }
        requireBinding().textInputEditSurnameMultipaySdk.onTextChanged { editText, text ->
            editText.checkSpaces(text)
        }
        requireBinding().textInputEditNameMultipaySdk.afterTextChanged {
            validate()
        }
        requireBinding().textInputEditSurnameMultipaySdk.afterTextChanged {
            validate()
        }

        maskWatcher = PhoneNumberTextWatcher(
            requireBinding().textInputEditGsmMultipaySdk,
            object : SimpleTextWatcher {
            }
        )
        requireBinding().textInputEditGsmMultipaySdk.addTextChangedListener(maskWatcher)

    }

    private fun validate() {
        // TODO : fill
    }

    private fun setLayoutProgressVisibility(visibility: Int) {
        requireBinding().registerProgressMultipaySdk.layoutProgressMultipaySdk.visibility =
            visibility
    }

}