package com.inventiv.multipaysdk.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.inventiv.multipaysdk.MultiPaySdk
import com.inventiv.multipaysdk.data.api.ApiService
import com.inventiv.multipaysdk.data.api.callback.NetworkCallback
import com.inventiv.multipaysdk.data.api.error.ApiError
import com.inventiv.multipaysdk.data.model.Event
import com.inventiv.multipaysdk.data.model.Resource
import com.inventiv.multipaysdk.data.model.request.ConfirmOtp
import com.inventiv.multipaysdk.data.model.response.ConfirmOtpResponse
import com.inventiv.multipaysdk.data.model.response.Result
import com.inventiv.multipaysdk.data.model.singleton.MultiPayUser

internal class OtpRepository(private val apiService: ApiService) {

    private val confirmOtpResult = MediatorLiveData<Event<Resource<ConfirmOtpResponse>>>()

    fun confirmOtp(
        confirmOtp: ConfirmOtp
    ): LiveData<Event<Resource<ConfirmOtpResponse>>> {

        confirmOtpResult.postValue(Event(Resource.Loading()))

        apiService.confirmOtpRequest(confirmOtp, object : NetworkCallback<Result> {
            override fun onSuccess(response: Result?) {
                val gson = MultiPaySdk.getComponent().gson()
                val confirmOtpResponse = gson.fromJson<ConfirmOtpResponse>(
                    response?.result,
                    ConfirmOtpResponse::class.java
                )
                MultiPayUser.authToken = confirmOtpResponse.authToken
                confirmOtpResult.postValue(Event(Resource.Success(confirmOtpResponse)))
            }

            override fun onError(error: ApiError) {
                confirmOtpResult.postValue(Event(Resource.Failure(error.message)))
            }
        })
        return confirmOtpResult
    }
}