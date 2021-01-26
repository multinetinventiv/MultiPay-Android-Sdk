package com.inventiv.multipaysdk.ui.wallet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import com.inventiv.multipaysdk.MultiPaySdk
import com.inventiv.multipaysdk.R
import com.inventiv.multipaysdk.base.BaseFragment
import com.inventiv.multipaysdk.data.model.EventObserver
import com.inventiv.multipaysdk.data.model.Resource
import com.inventiv.multipaysdk.data.model.singleton.MultiPayUser
import com.inventiv.multipaysdk.databinding.FragmentWalletBinding
import com.inventiv.multipaysdk.repository.WalletRepository
import com.inventiv.multipaysdk.ui.addwallet.AddWalletActivity
import com.inventiv.multipaysdk.ui.splash.SplashActivity
import com.inventiv.multipaysdk.util.*


internal class WalletFragment : BaseFragment<FragmentWalletBinding>() {

    companion object {
        fun newInstance() = WalletFragment()
    }

    private val viewModel: WalletViewModel by viewModels {
        WalletViewModelFactory(WalletRepository(MultiPaySdk.getComponent().apiService()))
    }

    private lateinit var listAdapter: WalletAdapter

    private var walletToken: String? = null

    override fun onResume() {
        super.onResume()
        showToolbar()
        hideToolbarBack()
        title(R.string.wallet_navigation_title)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentWalletBinding = FragmentWalletBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listAdapter = WalletAdapter(WalletListener { walletResponse ->
            viewModel.selectWallet(walletResponse)
        })
        prepareRecyclerView()
        subscribeWallet()
        subscribeMatchWallet()
        viewModel.walletsListItem()

        requireBinding().buttonAddWallet.setOnClickListener {
            startActivityForResult(
                AddWalletActivity.newIntent(requireActivity()),
                ADD_CARD_ACTVITY_REQUEST_CODE
            )
        }
        requireBinding().buttonMatch.setOnClickListener {
            walletToken =
                listAdapter.currentList.find { walletListItem -> walletListItem.isChecked }?.walletResponse?.token
            walletToken?.let { viewModel.matchWallet(it) }
        }
    }

    private fun prepareRecyclerView() {
        requireBinding().listWallets.apply {
            setHasFixedSize(true)
            adapter = listAdapter
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun subscribeSelectedWallet() {
        viewModel.selectedWallet.observe(viewLifecycleOwner, Observer { walletResponse ->
            requireBinding().buttonMatch.visibility = View.VISIBLE
            if (listAdapter.currentList.find { it.isChecked } == null) {
                val animSlideUp = AnimationUtils.loadAnimation(context, R.anim.anim_slide_up)
                requireBinding().buttonMatch.startAnimation(animSlideUp)
            }
            val newWalletItemList: MutableList<WalletListItem> = mutableListOf()
            listAdapter.currentList.forEach {
                newWalletItemList.add(
                    WalletListItem(
                        it.walletResponse,
                        it.walletResponse.token == walletResponse.token
                    )
                )
            }
            listAdapter.submitList(newWalletItemList)
        })
    }

    private fun subscribeWallet() {
        viewModel.walletsListItemResult.observe(viewLifecycleOwner, EventObserver { resource ->
            when (resource) {
                is Resource.Loading -> {
                    setLayoutProgressVisibility(View.VISIBLE)
                }
                is Resource.Success -> {
                    val walletList = resource.data
                    setLayoutProgressVisibility(View.GONE)
                    listAdapter.submitList(walletList?.toMutableList())
                    subscribeSelectedWallet()
                    showHideEmptyListText(walletList?.isEmpty() ?: false)
                }
                is Resource.Failure -> {
                    showSnackBarAlert(resource.message)
                    setLayoutProgressVisibility(View.GONE)
                    showHideEmptyListText(true)
                }
            }
        })
    }

    private fun subscribeMatchWallet() {
        viewModel.matchWalletResult.observe(viewLifecycleOwner, EventObserver { resource ->
            when (resource) {
                is Resource.Loading -> {
                    setLayoutProgressVisibility(View.VISIBLE)
                }
                is Resource.Success -> {
                    val matchWalletResponse = resource.data
                    MultiPayUser.walletToken = walletToken
                    setLayoutProgressVisibility(View.GONE)
                    requireActivity().sendBroadcast(
                        Intent(TOKEN_RECEIVED).apply {
                            putExtra(EXTRA_TOKEN_RECEIVED, walletToken)
                        }
                    )
                    startActivity(SplashActivity.newIntent(requireActivity(), isCancelled = true))
                }
                is Resource.Failure -> {
                    showSnackBarAlert(resource.message)
                    setLayoutProgressVisibility(View.GONE)
                }
            }
        })
    }

    private fun showHideEmptyListText(isShow: Boolean) {
        requireBinding().textWalletListEmpty.layoutCommonEmptyList.visibility =
            if (isShow) View.VISIBLE else View.GONE
        requireBinding().listWallets.visibility = if (isShow) View.GONE else View.VISIBLE
        requireBinding().textWalletListEmpty.textCommonEmptyList.text =
            getString(R.string.wallet_list_no_wallet)
    }

    private fun setLayoutProgressVisibility(visibility: Int) {
        requireBinding().walletProgress.layoutProgress.visibility = visibility
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_CARD_ACTVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                viewModel.walletsListItem()
            }
        }
    }
}