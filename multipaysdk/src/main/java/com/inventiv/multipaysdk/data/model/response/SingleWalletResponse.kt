package com.inventiv.multipaysdk.data.model.response

import com.google.gson.annotations.SerializedName

data class SingleWalletResponse(
    @field:SerializedName("wallet")
    val wallet: WalletResponse
)