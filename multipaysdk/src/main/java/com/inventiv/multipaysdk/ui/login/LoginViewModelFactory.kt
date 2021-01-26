package com.inventiv.multipaysdk.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inventiv.multipaysdk.repository.AuthenticationRepository

@Suppress("UNCHECKED_CAST")
internal class LoginViewModelFactory(
    private val authenticationRepository: AuthenticationRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (LoginViewModel(authenticationRepository) as T)
}