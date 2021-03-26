package com.inventiv.multipaysdk.data.model.singleton

sealed class MultiPaySdkException(message: String) : RuntimeException(message) {
    class SecurityException(message: String) : MultiPaySdkException(message)
}
