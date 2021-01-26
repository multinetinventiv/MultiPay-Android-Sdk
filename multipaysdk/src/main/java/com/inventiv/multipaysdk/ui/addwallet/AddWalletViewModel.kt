package com.inventiv.multipaysdk.ui.addwallet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.inventiv.multipaysdk.data.model.Event
import com.inventiv.multipaysdk.data.model.Resource
import com.inventiv.multipaysdk.data.model.request.AddWallet
import com.inventiv.multipaysdk.data.model.request.AddWalletRequest
import com.inventiv.multipaysdk.data.model.request.AuthAddWallet
import com.inventiv.multipaysdk.data.model.response.AddWalletResponse
import com.inventiv.multipaysdk.data.model.singleton.MultiPayUser
import com.inventiv.multipaysdk.repository.WalletRepository

internal class AddWalletViewModel(
    private val walletRepository: WalletRepository
) : ViewModel() {

    private val _addWallet = MutableLiveData<AddWalletRequest>()

    val addWalletResult: LiveData<Event<Resource<AddWalletResponse>>> =
        Transformations
            .switchMap(_addWallet) {
                walletRepository.addWallet(it)
            }

    fun addWallet(cardNumber: String, cvv: String, cardAlias: String) {
        _addWallet.value = if (MultiPayUser.walletToken.isNullOrEmpty()) {
            AuthAddWallet(
                number = cardNumber,
                cvv = cvv,
                alias = cardAlias
            )
        } else {
            AddWallet(
                number = cardNumber,
                cvv = cvv,
                alias = cardAlias
            )
        }
    }
}