package com.inventiv.multipaysdk.ui.addwallet

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.inventiv.multipaysdk.MultiPaySdk
import com.inventiv.multipaysdk.R
import com.inventiv.multipaysdk.base.BaseFragment
import com.inventiv.multipaysdk.data.model.EventObserver
import com.inventiv.multipaysdk.data.model.Resource
import com.inventiv.multipaysdk.databinding.FragmentAddWalletBinding
import com.inventiv.multipaysdk.repository.WalletRepository
import com.inventiv.multipaysdk.util.*
import com.inventiv.multipaysdk.view.listener.MaskCardNumberWatcher
import com.inventiv.multipaysdk.view.listener.MaskCardNumberWatcherView

internal class AddWalletFragment : BaseFragment<FragmentAddWalletBinding>(),
    MaskCardNumberWatcherView {

    private lateinit var viewModel: AddWalletViewModel

    companion object {
        fun newInstance(): AddWalletFragment = AddWalletFragment()
    }

    override fun onResume() {
        super.onResume()
        showToolbar()
        toolbarBack()
        title(R.string.add_card_navigation_title)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentAddWalletBinding = FragmentAddWalletBinding.inflate(inflater, container, false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModelFactory =
            AddWalletViewModelFactory(WalletRepository(MultiPaySdk.getComponent().apiService()))
        viewModel = ViewModelProvider(
            this@AddWalletFragment,
            viewModelFactory
        ).get(AddWalletViewModel::class.java)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeAddWallet()
        requireBinding().textInputEditCardAliasMultipaySdk.afterTextChanged { validate() }
        requireBinding().textInputEditCardNumberMultipaySdk.addTextChangedListener(
            MaskCardNumberWatcher(this@AddWalletFragment)
        )
        requireBinding().textInputEditCardCvvMultipaySdk.afterTextChanged { validate() }
        requireBinding().buttonContinueMultipaySdk.setOnClickListener {
            addWallet()
        }
    }

    override fun afterTextTriggered() {
        validate()
    }

    private fun subscribeAddWallet() {
        viewModel.addWalletResult.observe(viewLifecycleOwner, EventObserver { resource ->
            when (resource) {
                is Resource.Loading -> {
                    setLayoutProgressVisibility(View.VISIBLE)
                }
                is Resource.Success -> {
                    setLayoutProgressVisibility(View.GONE)
                    requireActivity().setResult(RESULT_OK)
                    requireActivity().finish()
                }
                is Resource.Failure -> {
                    showSnackBarAlert(resource.message)
                    setLayoutProgressVisibility(View.GONE)
                }
            }
        })
    }

    private fun setLayoutProgressVisibility(visibility: Int) {
        requireBinding().addCardProgressMultipaySdk.layoutProgressMultipaySdk.visibility =
            visibility
    }

    private fun addWallet() {
        requireBinding().textInputEditCardAliasMultipaySdk.hideKeyboard()
        requireBinding().textInputEditCardNumberMultipaySdk.hideKeyboard()
        requireBinding().textInputEditCardCvvMultipaySdk.hideKeyboard()
        val cardAlias = requireBinding().textInputEditCardAliasMultipaySdk.text.toString().trim()
        val cardNumber = requireBinding().textInputEditCardNumberMultipaySdk.text.toString().trim()
        val cvv = requireBinding().textInputEditCardCvvMultipaySdk.text.toString().trim()
        viewModel.addWallet(Formatter.stringToNumeric(cardNumber), cvv, cardAlias)
    }

    private fun validate() {
        val validCardAlias =
            requireBinding().textInputEditCardAliasMultipaySdk.text.toString().isNotEmpty()
        val validCardNumber =
            requireBinding().textInputEditCardNumberMultipaySdk.text.toString().isValidCardNumber()
        val validCardCvv =
            requireBinding().textInputEditCardCvvMultipaySdk.text.toString().isValidCvv()
        requireBinding().buttonContinueMultipaySdk.isEnabled =
            (validCardAlias && validCardNumber && validCardCvv)
    }
}