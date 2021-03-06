package com.inventiv.multipaysdk.ui.authentication.otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.inventiv.multipaysdk.data.model.Event
import com.inventiv.multipaysdk.data.model.Resource
import com.inventiv.multipaysdk.data.model.request.ConfirmOtp
import com.inventiv.multipaysdk.data.model.request.RegisterRequest
import com.inventiv.multipaysdk.data.model.response.ConfirmOtpResponse
import com.inventiv.multipaysdk.data.model.response.LoginResponse
import com.inventiv.multipaysdk.data.model.response.RegisterResponse
import com.inventiv.multipaysdk.repository.AuthenticationRepository
import com.inventiv.multipaysdk.repository.OtpRepository

internal class OtpViewModel(
    private val otpRepository: OtpRepository,
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _confirmOtp = MutableLiveData<Event<ConfirmOtp>>()
    private val _login = MutableLiveData<String>()
    private val _register = MutableLiveData<RegisterRequest>()

    val confirmOtpResult: LiveData<Event<Resource<ConfirmOtpResponse>>> =
        Transformations
            .switchMap(_confirmOtp) {
                otpRepository.confirmOtp(it.peekContent())
            }

    val loginResult: LiveData<Event<Resource<LoginResponse>>> =
        Transformations
            .switchMap(_login) {
                authenticationRepository.login(it)
            }

    val registerResult: LiveData<Event<Resource<RegisterResponse>>> =
        Transformations
            .switchMap(_register) {
                authenticationRepository.register(it)
            }

    fun confirmOtp(verificationCode: String?, otpCode: String) {
        _confirmOtp.value = Event(ConfirmOtp(verificationCode, otpCode))
    }

    fun login(emailOrGsm: String) {
        _login.value = emailOrGsm
    }

    fun register(registerRequest: RegisterRequest) {
        _register.value = registerRequest
    }
}