package com.inventiv.multipaysdk.data.api

import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonRequest
import com.google.gson.JsonSyntaxException
import com.inventiv.multipaysdk.MultiPaySdk
import com.inventiv.multipaysdk.data.api.NetworkManager.Companion.DEFAULT_TIMEOUT_MILLIS
import com.inventiv.multipaysdk.data.api.error.VolleyParseError
import com.inventiv.multipaysdk.data.model.request.BaseRequest
import com.inventiv.multipaysdk.data.model.response.BaseResponse
import com.inventiv.multipaysdk.data.model.type.RequestMethod

import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

internal class GsonRequest<I : BaseRequest, O : BaseResponse>(
    private val requestUrl: String,
    private val requestClass: I,
    private val responseClass: Class<O>,
    private val headers: MutableMap<String, String>?,
    private val responseListener: Response.Listener<O>,
    private val responseErrorListener: Response.ErrorListener,
    private val requestMethod: RequestMethod
) : JsonRequest<O>(
    requestMethod.method,
    requestUrl,
    MultiPaySdk.getComponent().gson().toJson(requestClass),
    responseListener,
    responseErrorListener
) {

    init {
        this.retryPolicy = DefaultRetryPolicy(DEFAULT_TIMEOUT_MILLIS, 1, 0.0f)
    }

    override fun getHeaders(): MutableMap<String, String> = headers ?: super.getHeaders()

    override fun deliverResponse(response: O) = responseListener.onResponse(response)

    override fun parseNetworkResponse(response: NetworkResponse?): Response<O> {

        return try {
            val json = String(
                response?.data ?: ByteArray(0),
                Charset.forName(HttpHeaderParser.parseCharset(response?.headers))
            )

            val model = MultiPaySdk.getComponent().gson().fromJson(json, responseClass)

            Response.success(
                model, HttpHeaderParser.parseCacheHeaders(response)
            )

        } catch (e: UnsupportedEncodingException) {
            Response.error(VolleyParseError(response))
        } catch (e: JsonSyntaxException) {
            Response.error(VolleyParseError(response))
        }
    }

    override fun toString(): String {
        return "GsonRequest(requestUrl=$requestUrl, " +
                "requestMethod=$requestMethod, " +
                "requestClass=($requestClass, walletAppToken=${requestClass.walletAppToken}, " +
                "languageCode=${requestClass.languageCode}), " +
                "headers=$headers)"
    }
}
