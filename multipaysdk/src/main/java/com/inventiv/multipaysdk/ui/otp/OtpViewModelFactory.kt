package com.inventiv.multipaysdk.ui.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inventiv.multipaysdk.repository.AuthenticationRepository
import com.inventiv.multipaysdk.repository.OtpRepository

@Suppress("UNCHECKED_CAST")
internal class OtpViewModelFactory(
    private val otpRepository: OtpRepository,
    private val authenticationRepository: AuthenticationRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (OtpViewModel(otpRepository, authenticationRepository) as T)
}