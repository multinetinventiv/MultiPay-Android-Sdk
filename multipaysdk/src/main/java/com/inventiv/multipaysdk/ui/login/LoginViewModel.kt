package com.inventiv.multipaysdk.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.inventiv.multipaysdk.data.model.Event
import com.inventiv.multipaysdk.data.model.Resource
import com.inventiv.multipaysdk.data.model.response.LoginResponse
import com.inventiv.multipaysdk.repository.AuthenticationRepository

internal class LoginViewModel(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _login = MutableLiveData<Pair<String, String>>()

    val loginResult: LiveData<Event<Resource<LoginResponse>>> =
        Transformations
            .switchMap(_login) {
                authenticationRepository.login(it.first, it.second)
            }

    fun login(emailOrGsm: String, password: String) {
        _login.value = Pair(emailOrGsm, password)
    }

}