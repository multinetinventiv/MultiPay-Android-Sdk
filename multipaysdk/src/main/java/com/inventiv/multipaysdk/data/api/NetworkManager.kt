package com.inventiv.multipaysdk.data.api

import com.google.gson.JsonSyntaxException
import com.inventiv.multipaysdk.Environment
import com.inventiv.multipaysdk.MultiPaySdk
import com.inventiv.multipaysdk.data.api.callback.NetworkCallback
import com.inventiv.multipaysdk.data.api.error.ApiError
import com.inventiv.multipaysdk.data.model.request.BaseRequest
import com.inventiv.multipaysdk.data.model.response.BaseResponse
import com.inventiv.multipaysdk.data.model.response.Result
import com.inventiv.multipaysdk.data.model.type.RequestMethod
import java.util.concurrent.TimeUnit

internal class NetworkManager(private val volleyManager: VolleyManager, environment: Environment) {

    companion object {
        val DEFAULT_TIMEOUT_MILLIS = TimeUnit.MINUTES.toMillis(1).toInt()

        const val PRIORITY_LOW = 0
        const val PRIORITY_NORMAL = 1
        const val PRIORITY_HIGH = 2
        const val PRIORITY_IMMEDIATE = 3

        private const val HEADER_KEY_OS_VERSION = "device-os-version"
        private const val HEADER_VALUE_OS_VERSION = "Android"
    }

    private val baseUrl = environment.baseUrl
    private val apiServicePath = environment.apiServicePath

    fun <T : BaseResponse> sendRequest(
        request: BaseRequest,
        requestPath: String,
        responseModel: Class<T>,
        networkCallback: NetworkCallback<T>,
        requestMethod: RequestMethod = RequestMethod.POST,
        requestBaseUrl: String = baseUrl,
        requestApiServicePath: String = apiServicePath
    ) {

        val headers = mutableMapOf<String, String>()
        headers[HEADER_KEY_OS_VERSION] = HEADER_VALUE_OS_VERSION

        volleyManager.sendRequest(
            "$requestBaseUrl$requestApiServicePath",
            request,
            requestPath,
            headers,
            responseModel,
            object : NetworkCallback<T> {
                override fun onSuccess(response: T?) {
                    handleNetworkResponse(response, null, networkCallback)
                }

                override fun onError(volleyError: ApiError) {
                    handleNetworkResponse(null, volleyError, networkCallback)
                }
            },
            requestMethod
        )
    }

    private fun <T : BaseResponse> handleNetworkResponse(
        response: T?,
        volleyError: ApiError?,
        networkCallback: NetworkCallback<T>
    ) {
        volleyError?.let {
            val strData = it.data?.toString(Charsets.UTF_8)
            if (strData.isNullOrEmpty()) {
                deliverResponse(null, volleyError, networkCallback)
                return
            }

            try {
                val result = MultiPaySdk.getComponent().gson().fromJson(strData, Result::class.java)
                val apiError =
                    ApiError.apiErrorInstance(result.resultMessage, result.resultCode, result)
                deliverResponse(null, apiError, networkCallback)
            } catch (e: JsonSyntaxException) {
                deliverResponse(null, volleyError, networkCallback)
            }

            return
        }

        if (response !is Result) {
            deliverResponse(response, null, networkCallback)
            return
        }

        if (response.isSuccess()) {
            deliverResponse(response, null, networkCallback)
        } else {
            val apiError =
                ApiError.apiErrorInstance(response.resultMessage, response.resultCode, response)
            deliverResponse(null, apiError, networkCallback)
        }
    }

    private fun <T : BaseResponse> deliverResponse(
        response: T?,
        apiError: ApiError?,
        callback: NetworkCallback<T>
    ) {
        apiError?.let {
            callback.onError(it)
            return
        }
        callback.onSuccess(response)
    }
}
