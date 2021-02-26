package com.inventiv.multipaysdk.ui.addwallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inventiv.multipaysdk.repository.WalletRepository

@Suppress("UNCHECKED_CAST")
internal class AddWalletViewModelFactory(
    private val walletRepository: WalletRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (AddWalletViewModel(walletRepository) as T)
}