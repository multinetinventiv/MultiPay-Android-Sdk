package com.inventiv.multipaysdk.data.model.response

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import com.inventiv.multipaysdk.data.model.type.ResultCode

internal data class Result(
    @field:SerializedName("result")
    var result: JsonElement? = null,
    @field:SerializedName("resultCode")
    var resultCode: Int = 0,
    @field:SerializedName("resultMessage")
    var resultMessage: String? = null
) : BaseResponse {
    fun isSuccess(): Boolean {
        return resultCode == ResultCode.SUCCESS.value
    }
}