package com.inventiv.multipaysdk.data.api.error

import com.android.volley.NetworkResponse
import com.android.volley.VolleyError

internal class VolleyParseError(response: NetworkResponse?) : VolleyError(response)