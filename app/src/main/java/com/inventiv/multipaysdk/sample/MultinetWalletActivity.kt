package com.inventiv.multipaysdk.sample

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.inventiv.multipaysdk.MultiPaySdk
import com.inventiv.multipaysdk.MultiPaySdkListener
import com.inventiv.multipaysdk.data.model.request.TransactionDetail
import com.inventiv.multipaysdk.data.model.response.SingleWalletResponse
import com.inventiv.multipaysdk.data.model.response.UnselectWalletResponse
import com.inventiv.multipaysdk.data.model.response.WalletResponse
import com.inventiv.multipaysdk.data.model.singleton.MultiPaySdkException

class MultinetWalletActivity : AppCompatActivity(), ConfirmPaymentDialogListener,
    PaymentRollbackDialogListener {

    companion object {
        const val EXTRA_INFOS = "extra_infos"
        const val TAG = "MultinetWalletActivity"

        fun newIntent(context: Context, infos: Infos) =
            Intent(context, MultinetWalletActivity::class.java).apply {
                putExtra(EXTRA_INFOS, infos)
            }
    }

    private lateinit var walletItem: ConstraintLayout
    private lateinit var paymentItem: ConstraintLayout
    private lateinit var btnDeleteInfos: Button
    private lateinit var btnStartSdk: Button
    private lateinit var btnStartSdkWithUserPreset: Button


    private lateinit var sampleReceiver: SampleReceiver
    private lateinit var info: Infos
    private var walletToken: String? = null
    private var walletResponse: WalletResponse? = null
    private var latestPaymentInfos: PaymentInfos? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multinet_wallet)

        info = intent.getParcelableExtra(EXTRA_INFOS)!!

        walletItem = findViewById(R.id.included_wallet_item)
        btnDeleteInfos = findViewById(R.id.btn_delete_infos)
        btnStartSdk = findViewById(R.id.btn_start_sdk)
        btnStartSdkWithUserPreset = findViewById(R.id.btn_start_sdk_with_user_preset)
        paymentItem = findViewById(R.id.included_payment_item)

        sampleReceiver = SampleReceiver(multiPaySdkListener!!)
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.inventiv.multipaysdk.intent.TOKEN_RECEIVED")
        intentFilter.addAction("com.inventiv.multipaysdk.intent.SDK_CLOSED")
        registerReceiver(sampleReceiver, intentFilter)

        try {
            MultiPaySdk.init(
                context = this.applicationContext,
                walletAppToken = info.walletApptoken,
                paymentAppToken = info.paymentAppToken,
                saltKey = info.saltKey,
                environment = info.environment,
                userId = info.userID
            )
        } catch (exception: MultiPaySdkException) {
            Toast.makeText(
                this@MultinetWalletActivity,
                "multipaySdkError : ${exception.message}",
                Toast.LENGTH_LONG
            ).show()
            onDeleteInfosClicked()
            return@onCreate
        }


        val strWalletToken = getSharedPref().getString(PREF_WALLET_TOKEN, String())
        if (!strWalletToken.isNullOrEmpty()) {
            walletToken = strWalletToken
            getCardInfo(walletToken!!, multiPaySdkListener!!)
        }

        val strPaymentInfos = getSharedPref().getString(PREF_PAYMENT_INFOS, String())
        if (!strPaymentInfos.isNullOrEmpty()) {
            latestPaymentInfos = Gson().fromJson(strPaymentInfos, PaymentInfos::class.java)
            latestPaymentInfos?.let {
                showLatestPayment(it)
            }
        }

        bindOnClickEvents()
    }

    override fun onConfirmPayment(paymentInfos: PaymentInfos) {
        latestPaymentInfos = paymentInfos

        MultiPaySdk.confirmPayment(
            walletToken = walletToken!!,
            requestId = paymentInfos.requestId,
            terminalReferenceNumber = paymentInfos.terminalReferenceNumber,
            merchantReferenceNumber = paymentInfos.merchantReferenceNumber,
            transferReferenceNumber = paymentInfos.transferReferenceNumber,
            transactionDetails = listOf(
                TransactionDetail(
                    amount = paymentInfos.amount,
                    productId = paymentInfos.productId,
                    referenceNumber = paymentInfos.referenceNumber
                )
            ),
            sign = paymentInfos.sign,
            listener = multiPaySdkListener!!
        )
    }

    override fun onPaymentRollback(paymentRollback: PaymentRollback) {
        MultiPaySdk.rollbackPayment(
            requestId = paymentRollback.requestId,
            sign = paymentRollback.sign,
            merchantReferenceNumber = paymentRollback.merchantReferenceNumber,
            terminalReferenceNumber = paymentRollback.terminalReferenceNumber,
            rollbackReferenceNumber = paymentRollback.rollbackReferenceNumber,
            reason = paymentRollback.reason,
            referenceNumberType = paymentRollback.referenceNumberType,
            referenceNumber = paymentRollback.referenceNumber,
            listener = multiPaySdkListener!!
        )
    }

    private fun bindOnClickEvents() {
        btnDeleteInfos.setOnClickListener {
            onDeleteInfosClicked()
        }

        btnStartSdk.setOnClickListener {
            callStartSdkMethod()
        }

        btnStartSdkWithUserPreset.setOnClickListener {
            callStartWithUserPresetSdkMethod()
        }

        (walletItem.findViewById(R.id.button_change_wallet) as Button).setOnClickListener {
            onCardChangeClicked()
        }

        (walletItem.findViewById(R.id.button_delete_wallet) as Button).setOnClickListener {
            onCardDeleteClicked()
        }

        (walletItem.findViewById(R.id.button_confirm_payment) as Button).setOnClickListener {
            onConfirmPaymentClicked()
        }
    }

    private var multiPaySdkListener: MultiPaySdkListener? = object : MultiPaySdkListener {
        override fun onTokenReceived(token: String) {
            Log.i(TAG, "${info.environment.name} onTokenReceived: $token")
            walletToken = token
            getSharedPref().edit().putString(PREF_WALLET_TOKEN, token).apply()
            getCardInfo(token, this)
        }

        override fun onMultiPaySdkClosed() {
            Log.i(TAG, "${info.environment.name} onMultiPaySdkClosed")
        }

        override fun onSingeWalletReceived(singleWallet: SingleWalletResponse) {
            Log.i(TAG, "${info.environment.name} onSingeWalletReceived: $singleWallet")
            walletResponse = singleWallet.wallet
            setWalletUI()
            walletItem.visibility = View.VISIBLE
            btnStartSdk.visibility = View.GONE
            btnStartSdkWithUserPreset.visibility = View.GONE
        }

        override fun onUnSelectWalletReceived(unSelectWallet: UnselectWalletResponse?) {
            Log.d(TAG, "${info.environment.name} onUnSelectWalletReceived: $unSelectWallet")
            walletResponse = null
            walletItem.visibility = View.GONE
            getSharedPref().edit().remove(PREF_WALLET_TOKEN).apply()
            btnStartSdk.visibility = View.VISIBLE
            btnStartSdkWithUserPreset.visibility = View.VISIBLE
        }

        override fun onServiceError(error: String?, code: Int) {
            Log.d(TAG, "${info.environment.name} onServiceError: error = $error code = $code")
            Toast.makeText(
                this@MultinetWalletActivity,
                "onServiceError : $error",
                Toast.LENGTH_LONG
            ).show()
            latestPaymentInfos?.let {
                showLatestPayment(it)
            }
        }

        override fun onConfirmPaymentReceived(sign: String, transferServerRefNo: String) {
            // Save payment info for later use on refund
            latestPaymentInfos?.let {
                it.transferServerRefNo = transferServerRefNo
                val strInfos = Gson().toJson(it)
                getSharedPref().edit().putString(PREF_PAYMENT_INFOS, strInfos).apply()
                showLatestPayment(it)
            }

            Log.d(
                TAG,
                "${info.environment.name} onConfirmPaymentReceived: $sign  $transferServerRefNo"
            )
            getCardInfo(walletToken!!, this)
        }

        override fun onRollbackPaymentReceived(
            sign: String,
            rollbackServerReferenceNumber: String
        ) {
            Log.d(
                TAG,
                "${info.environment.name} onRollbackPaymentReceived: $sign  $rollbackServerReferenceNumber"
            )
            getCardInfo(walletToken!!, this)
        }
    }

    private fun showLatestPayment(paymentInfos: PaymentInfos) {
        (paymentItem.findViewById(R.id.text_payment_transfer_reference_number) as TextView).text =
            "TransferReferenceNumber : ${paymentInfos.transferReferenceNumber}"
        (paymentItem.findViewById(R.id.text_payment_amount) as TextView).text =
            "Amount : ${paymentInfos.amount}"
        (paymentItem.findViewById(R.id.text_payment_product_id) as TextView).text =
            "ProductId : ${paymentInfos.productId}"

        (paymentItem.findViewById(R.id.button_payment_cancel) as Button).setOnClickListener {
            paymentInfos.reason = RollbackPaymentType.CANCEL.code
            showPaymentRollbackDialog(paymentInfos)
        }
        (paymentItem.findViewById(R.id.button_payment_refund) as Button).setOnClickListener {
            paymentInfos.reason = RollbackPaymentType.REFUND.code
            showPaymentRollbackDialog(paymentInfos)
        }
        (paymentItem.findViewById(R.id.button_payment_reversal) as Button).setOnClickListener {
            paymentInfos.reason = RollbackPaymentType.REVERSAL.code
            showPaymentRollbackDialog(paymentInfos)
        }

        paymentItem.visibility = View.VISIBLE
    }

    private fun showPaymentRollbackDialog(paymentInfos: PaymentInfos) {
        PaymentRollbackDialogFragment.newInstance(paymentInfos).show(
            supportFragmentManager,
            "FRAGMENT_PAYMENT_ROLLBACK_DIALOG"
        )
    }

    private fun setWalletUI() {
        (walletItem.findViewById(R.id.text_wallet_name_multipay_sdk) as TextView).text =
            walletResponse?.name
        (walletItem.findViewById(R.id.text_wallet_balance_multipay_sdk) as TextView).text =
            walletResponse?.balance
        (walletItem.findViewById(R.id.text_wallet_number_multipay_sdk) as TextView).text =
            walletResponse?.maskedNumber
        val walletImageView = (walletItem.findViewById(R.id.image_wallet_multipay_sdk) as ImageView)
        Glide.with(this).load(walletResponse?.imageUrl).into(walletImageView)
    }

    private fun callStartSdkMethod() {
        MultiPaySdk.start(this, null)
    }

    private fun callStartWithUserPresetSdkMethod() {
        UserPresetDialog().show(
            supportFragmentManager,
            null
        )
    }

    private fun onCardChangeClicked() {
        MultiPaySdk.start(this, walletToken!!)
    }

    private fun getCardInfo(token: String, multiPaySdkListener: MultiPaySdkListener) {
        MultiPaySdk.getWallet(token, multiPaySdkListener)
    }

    private fun onCardDeleteClicked() {
        MultiPaySdk.unselectWallet(walletResponse?.token!!, multiPaySdkListener!!)
    }

    private fun onConfirmPaymentClicked() {
        ConfirmPaymentDialogFragment().show(
            supportFragmentManager,
            "FRAGMENT_CONFIRM_PAYMENT_DIALOG"
        )
    }

    private fun onDeleteInfosClicked() {
        walletResponse?.token?.let { token ->
            MultiPaySdk.unselectWallet(token, multiPaySdkListener!!)
        }
        getSharedPref().edit().clear().apply()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        multiPaySdkListener = null
        unregisterReceiver(sampleReceiver)
        super.onDestroy()
    }
}