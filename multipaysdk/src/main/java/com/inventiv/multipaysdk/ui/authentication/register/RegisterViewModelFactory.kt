package com.inventiv.multipaysdk.ui.authentication.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inventiv.multipaysdk.repository.AuthenticationRepository

@Suppress("UNCHECKED_CAST")
internal class RegisterViewModelFactory(
    private val authenticationRepository: AuthenticationRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (RegisterViewModel(authenticationRepository) as T)
}
