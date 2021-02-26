package com.inventiv.multipaysdk.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.inventiv.multipaysdk.MultiPaySdk
import com.inventiv.multipaysdk.data.api.ApiService
import com.inventiv.multipaysdk.data.api.callback.NetworkCallback
import com.inventiv.multipaysdk.data.api.error.ApiError
import com.inventiv.multipaysdk.data.model.Event
import com.inventiv.multipaysdk.data.model.Resource
import com.inventiv.multipaysdk.data.model.request.AddWalletRequest
import com.inventiv.multipaysdk.data.model.request.AuthAddWallet
import com.inventiv.multipaysdk.data.model.response.AddWalletResponse
import com.inventiv.multipaysdk.data.model.response.MatchWalletResponse
import com.inventiv.multipaysdk.data.model.response.Result
import com.inventiv.multipaysdk.data.model.response.WalletsResponse
import com.inventiv.multipaysdk.ui.wallet.WalletListItem

internal class WalletRepository(private val apiService: ApiService) {

    private val addWalletResult =
        MediatorLiveData<Event<Resource<AddWalletResponse>>>()

    private val walletsResult =
        MediatorLiveData<Event<Resource<List<WalletListItem>>>>()
    private val matchWalletResult =
        MediatorLiveData<Event<Resource<MatchWalletResponse>>>()

    fun addWallet(addWalletRequest: AddWalletRequest): LiveData<Event<Resource<AddWalletResponse>>> {

        addWalletResult.postValue(Event(Resource.Loading()))

        apiService.addWalletRequest(addWalletRequest, object : NetworkCallback<Result> {
            override fun onSuccess(response: Result?) {
                val gson = MultiPaySdk.getComponent().gson()
                val addWalletResponse = gson.fromJson<AddWalletResponse>(
                    response?.result,
                    AuthAddWallet::class.java
                )
                addWalletResult.postValue(Event(Resource.Success(addWalletResponse)))
            }

            override fun onError(error: ApiError) {
                addWalletResult.postValue(Event(Resource.Failure(error.message)))
            }
        })

        return addWalletResult
    }

    fun walletsListItem(): LiveData<Event<Resource<List<WalletListItem>>>> {

        walletsResult.postValue(Event(Resource.Loading()))

        apiService.walletRequest(object : NetworkCallback<Result> {
            override fun onSuccess(response: Result?) {
                val gson = MultiPaySdk.getComponent().gson()
                val walletsResponse = gson.fromJson<WalletsResponse>(
                    response?.result,
                    WalletsResponse::class.java
                )
                walletsResult.postValue(
                    Event(Resource.Success(mapWalletsResponseToWalletListItem(walletsResponse)))
                )
            }

            override fun onError(error: ApiError) {
                walletsResult.postValue(Event(Resource.Failure(error.message)))
            }
        })

        return walletsResult
    }

    fun mapWalletsResponseToWalletListItem(walletsResponse: WalletsResponse?): List<WalletListItem> {
        val walletListItemList: MutableList<WalletListItem> = mutableListOf()
        walletsResponse?.wallets?.forEach {
            val walletListItem = if (it.isSelected != null) {
                WalletListItem(it, it.isSelected)
            } else {
                WalletListItem(it)
            }
            walletListItemList.add(walletListItem)
        }
        return walletListItemList
    }

    fun matchWallet(walletToken: String): LiveData<Event<Resource<MatchWalletResponse>>> {

        matchWalletResult.postValue(Event(Resource.Loading()))

        apiService.matchWalletRequest(walletToken, object : NetworkCallback<Result> {
            override fun onSuccess(response: Result?) {
                val gson = MultiPaySdk.getComponent().gson()
                val matchWalletResponse = gson.fromJson<MatchWalletResponse>(
                    response?.result,
                    MatchWalletResponse::class.java
                )
                matchWalletResult.postValue(
                    Event(Resource.Success(matchWalletResponse))
                )
            }

            override fun onError(error: ApiError) {
                matchWalletResult.postValue(Event(Resource.Failure(error.message)))
            }
        })

        return matchWalletResult
    }
}