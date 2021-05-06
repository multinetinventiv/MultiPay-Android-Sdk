package com.inventiv.multipaysdk.ui.authentication.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.inventiv.multipaysdk.MultiPaySdk
import com.inventiv.multipaysdk.R
import com.inventiv.multipaysdk.base.BaseFragment
import com.inventiv.multipaysdk.databinding.FragmentRegisterMultipaySdkBinding
import com.inventiv.multipaysdk.repository.AuthenticationRepository
import com.inventiv.multipaysdk.util.*


internal class RegisterFragment : BaseFragment<FragmentRegisterMultipaySdkBinding>() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewModel: RegisterViewModel

    private var walletToken: String? = null

    override fun onResume() {
        super.onResume()
        showToolbar()
        toolbarBack()
        title(R.string.register_header_multipay_sdk)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentRegisterMultipaySdkBinding =
        FragmentRegisterMultipaySdkBinding.inflate(inflater, container, false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModelFactory = RegisterViewModelFactory(
            AuthenticationRepository(
                MultiPaySdk.getComponent().apiService()
            )
        )

        viewModel = ViewModelProvider(
            this@RegisterFragment,
            viewModelFactory
        ).get(RegisterViewModel::class.java)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar().setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        /*subscribeWallet()
        subscribeMatchWallet()

        requireBinding().buttonAddWalletMultipaySdk.setOnClickListener {
            startActivityForResult(
                AddWalletActivity.newIntent(requireActivity()),
                ADD_CARD_ACTVITY_REQUEST_CODE
            )
        }
        requireBinding().buttonMatchMultipaySdk.setOnClickListener {
            walletToken =
                listAdapter.currentList.find { walletListItem -> walletListItem.isChecked }?.walletResponse?.token
            walletToken?.let { viewModel.matchWallet(it) }
        }*/
    }

    /*private fun subscribeSelectedWallet() {
        viewModel.selectedWallet.observe(viewLifecycleOwner, Observer { walletResponse ->
            requireBinding().buttonMatchMultipaySdk.visibility = View.VISIBLE
            if (listAdapter.currentList.find { it.isChecked } == null) {
                val animSlideUp =
                    AnimationUtils.loadAnimation(context, R.anim.anim_slide_up_multipay_sdk)
                requireBinding().buttonMatchMultipaySdk.startAnimation(animSlideUp)
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

    private fun setLayoutProgressVisibility(visibility: Int) {
        requireBinding().walletProgressMultipaySdk.layoutProgressMultipaySdk.visibility = visibility
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_CARD_ACTVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                viewModel.walletsListItem()
            }
        }
    }*/
}