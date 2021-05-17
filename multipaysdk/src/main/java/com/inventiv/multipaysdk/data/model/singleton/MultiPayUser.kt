package com.inventiv.multipaysdk.data.model.singleton

import com.inventiv.multipaysdk.data.model.response.UserPreset

internal object MultiPayUser {
    var authToken: String? = null
    var walletToken: String? = null
    var userPreset: UserPreset? = null
}