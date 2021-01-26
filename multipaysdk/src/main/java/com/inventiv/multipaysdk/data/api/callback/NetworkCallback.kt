package com.inventiv.multipaysdk.data.api.callback

import com.inventiv.multipaysdk.data.api.error.ApiError
import com.inventiv.multipaysdk.data.model.response.BaseResponse

internal interface NetworkCallback<T : BaseResponse> {
    fun onSuccess(response: T?)
    fun onError(error: ApiError)
}