package com.inventiv.multipaysdk.ui.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inventiv.multipaysdk.repository.WalletRepository

@Suppress("UNCHECKED_CAST")
internal class WalletViewModelFactory(
    private val walletRepository: WalletRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (WalletViewModel(walletRepository) as T)
}
