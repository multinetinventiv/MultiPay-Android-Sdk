package com.inventiv.multipaysdk.ui.authentication.contract

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.inventiv.multipaysdk.base.BaseFragment
import com.inventiv.multipaysdk.databinding.FragmentContractMultipaySdkBinding
import com.inventiv.multipaysdk.util.*

internal class ContractFragment : BaseFragment<FragmentContractMultipaySdkBinding>() {

    private var webTitle: String? = null
    private var webUrl: String? = null
    private var buttonVisibility: Boolean? = null

    companion object {
        fun newInstance(
            title: String,
            url: String,
            buttonVisibility: Boolean
        ): ContractFragment =
            ContractFragment().apply {
                val args = Bundle().apply {
                    putString(ARG_WEB_TITLE, title)
                    putString(ARG_WEB_URL, url)
                    putBoolean(ARG_BUTTON_VISIBILITY, buttonVisibility)
                }
                arguments = args
            }
    }

    override fun onResume() {
        super.onResume()
        showToolbar()
        toolbarClose()
        title(webTitle ?: String())
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentContractMultipaySdkBinding {
        return FragmentContractMultipaySdkBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webTitle = arguments?.getString(ARG_WEB_TITLE)
        webUrl = arguments?.getString(ARG_WEB_URL)
        buttonVisibility = arguments?.getBoolean(ARG_BUTTON_VISIBILITY)

        requireBinding().layoutButtonsMultipaySdk.visibility =
            if (buttonVisibility == true) {
                View.VISIBLE
            } else {
                View.GONE
            }

        webUrl?.let {
            requireBinding().webViewMultipaySdk.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    setLayoutProgressVisibility(View.VISIBLE)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    setLayoutProgressVisibility(View.GONE)
                }
            }
            requireBinding().webViewMultipaySdk.loadUrl(it)
        }

        requireBinding().materialButtonAgreeMultipaySdk.setOnClickListener {
            requireActivity().setResult(Activity.RESULT_OK)
            requireActivity().finish()
        }
        requireBinding().materialButtonCancelMultipaySdk.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun setLayoutProgressVisibility(visibility: Int) {
        try {
            requireBinding().progressContractMultipaySdk.layoutProgressMultipaySdk.visibility =
                visibility
        }catch (e : Exception){
            Log.e("ContactFragment", e.toString())
        }
    }
}