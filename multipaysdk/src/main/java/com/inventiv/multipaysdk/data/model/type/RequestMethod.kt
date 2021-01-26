package com.inventiv.multipaysdk.data.model.type

import com.android.volley.Request

internal enum class RequestMethod(val method: Int) {
    GET(Request.Method.GET),
    POST(Request.Method.POST),
    PUT(Request.Method.PUT),
    DELETE(Request.Method.DELETE),
    HEAD(Request.Method.HEAD),
    OPTIONS(Request.Method.OPTIONS),
    TRACE(Request.Method.TRACE),
    PATCH(Request.Method.PATCH)
}