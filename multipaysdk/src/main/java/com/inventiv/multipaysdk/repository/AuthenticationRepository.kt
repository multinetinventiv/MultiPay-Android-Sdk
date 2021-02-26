package com.inventiv.multipaysdk.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.inventiv.multipaysdk.MultiPaySdk
import com.inventiv.multipaysdk.data.api.ApiService
import com.inventiv.multipaysdk.data.api.callback.NetworkCallback
import com.inventiv.multipaysdk.data.api.error.ApiError
import com.inventiv.multipaysdk.data.model.Event
import com.inventiv.multipaysdk.data.model.Resource
import com.inventiv.multipaysdk.data.model.request.LoginEmail
import com.inventiv.multipaysdk.data.model.request.LoginGsm
import com.inventiv.multipaysdk.data.model.response.LoginResponse
import com.inventiv.multipaysdk.data.model.response.Result
import com.inventiv.multipaysdk.data.model.type.ValidationErrorType
import com.inventiv.multipaysdk.util.Formatter
import com.inventiv.multipaysdk.util.Validator

internal class AuthenticationRepository(private val apiService: ApiService) {

    private val loginResult = MediatorLiveData<Event<Resource<LoginResponse>>>()

    fun login(emailOrGsm: String, password: String): LiveData<Event<Resource<LoginResponse>>> {

        val validEmail: Boolean = Validator.validEmail(emailOrGsm)
        val validGsm: Boolean = Validator.validGsmWithMask(emailOrGsm)
        val validPassword: Boolean = Validator.validPassword(password)
        val loginInputType: Int = Validator.getInputType(emailOrGsm)

        val loginRequest = if (loginInputType == Validator.INPUT_TYPE_GSM) {
            LoginGsm(Formatter.servicePhoneNumber(emailOrGsm), password)
        } else {
            LoginEmail(emailOrGsm, password)
        }

        if ((validEmail || validGsm) && validPassword) {

            loginResult.postValue(Event(Resource.Loading()))

            apiService.loginRequest(loginRequest, object : NetworkCallback<Result> {
                override fun onSuccess(response: Result?) {
                    val gson = MultiPaySdk.getComponent().gson()
                    val otpResponse = gson.fromJson<LoginResponse>(
                        response?.result,
                        LoginResponse::class.java
                    )
                    loginResult.postValue(Event(Resource.Success(otpResponse)))
                }

                override fun onError(error: ApiError) {
                    loginResult.postValue(Event(Resource.Failure(error.message)))
                }
            })

        } else {

            var type: ValidationErrorType = ValidationErrorType.ALL

            if (!validPassword) {
                type = ValidationErrorType.PASSWORD
            }
            if (loginInputType == Validator.INPUT_TYPE_GSM && !validGsm) {
                type = ValidationErrorType.GSM
            }
            if (loginInputType == Validator.INPUT_TYPE_EMAIL && !validEmail) {
                type = ValidationErrorType.EMAIL
            }
            if (loginInputType == Validator.INPUT_TYPE_UNDEFINED && !validEmail && !validGsm) {
                type = ValidationErrorType.EMAIL_GSM
            }
            val message: String = Validator.getValidationError(type)
            loginResult.postValue(Event(Resource.Failure(message)))
        }

        return loginResult
    }
}