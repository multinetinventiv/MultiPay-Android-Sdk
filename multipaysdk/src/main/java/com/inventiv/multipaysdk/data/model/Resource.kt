package com.inventiv.multipaysdk.data.model

import com.inventiv.multipaysdk.data.api.error.ApiError

internal sealed class Resource<out T> {
    class Loading<out T> : Resource<T>()
    data class Success<out T>(val data: T?) : Resource<T>()
    data class Failure<out T>(val error: ApiError) : Resource<T>()
}