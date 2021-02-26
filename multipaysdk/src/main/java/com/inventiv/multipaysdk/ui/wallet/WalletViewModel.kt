package com.inventiv.multipaysdk.ui.wallet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.inventiv.multipaysdk.data.model.Event
import com.inventiv.multipaysdk.data.model.Resource
import com.inventiv.multipaysdk.data.model.response.MatchWalletResponse
import com.inventiv.multipaysdk.data.model.response.WalletResponse
import com.inventiv.multipaysdk.repository.WalletRepository

internal class WalletViewModel(
    private val walletRepository: WalletRepository
) : ViewModel() {

    private val _walletsListItem = MutableLiveData<Unit>()
    private val _matchWallet = MutableLiveData<String>()

    var selectedWallet = MutableLiveData<WalletResponse>()

    val walletsListItemResult: LiveData<Event<Resource<List<WalletListItem>>>> =
        Transformations
            .switchMap(_walletsListItem) {
                walletRepository.walletsListItem()
            }

    val matchWalletResult: LiveData<Event<Resource<MatchWalletResponse>>> =
        Transformations
            .switchMap(_matchWallet) {
                walletRepository.matchWallet(it)
            }

    fun walletsListItem() {
        _walletsListItem.value = Unit
    }

    fun matchWallet(walletToken: String) {
        _matchWallet.value = walletToken
    }

    fun selectWallet(walletResponse: WalletResponse) {
        selectedWallet.value = walletResponse
    }

}