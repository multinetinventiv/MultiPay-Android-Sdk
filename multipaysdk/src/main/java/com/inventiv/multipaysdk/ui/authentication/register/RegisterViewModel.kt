package com.inventiv.multipaysdk.ui.authentication.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.inventiv.multipaysdk.data.model.Event
import com.inventiv.multipaysdk.data.model.Resource
import com.inventiv.multipaysdk.data.model.request.BaseRequest
import com.inventiv.multipaysdk.data.model.request.RegisterRequest
import com.inventiv.multipaysdk.data.model.response.AgreementsResponse
import com.inventiv.multipaysdk.data.model.response.RegisterResponse
import com.inventiv.multipaysdk.repository.AuthenticationRepository

internal class RegisterViewModel(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _register = MutableLiveData<RegisterRequest>()

    private val _agreements = MutableLiveData<BaseRequest>()

    val registerResult: LiveData<Event<Resource<RegisterResponse>>> =
        Transformations
            .switchMap(_register) {
                authenticationRepository.register(it)
            }

    val agreementsResult: LiveData<Event<Resource<AgreementsResponse>>> =
        Transformations
            .switchMap(_agreements) {
                authenticationRepository.agreements()
            }

    fun register(
        name: String,
        surname: String,
        email: String,
        gsm: String,
        isNotificationAccepted: Boolean
    ) {
        _register.value = RegisterRequest(name, surname, email, gsm, isNotificationAccepted)
    }

    fun agreements() {
        _agreements.value = BaseRequest()
    }

}