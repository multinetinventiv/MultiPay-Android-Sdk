package com.inventiv.multipaysdk.sample

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.inventiv.multipaysdk.MultiPaySdk
import com.inventiv.multipaysdk.MultiPaySdkListener

class MultinetWalletActivity : AppCompatActivity() {

    companion object {

        const val EXTRA_INFOS = "extra_infos"
        const val TAG = "MultinetWalletActivity"

        fun newIntent(context: Context, infos: Infos) =
            Intent(context, MultinetWalletActivity::class.java).apply {
                putExtra(EXTRA_INFOS, infos)
            }
    }

    private lateinit var sampleReceiver: SampleReceiver
    private lateinit var info: Infos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multinet_wallet)

        info = intent.getParcelableExtra(EXTRA_INFOS)!!

        sampleReceiver = SampleReceiver(multiPaySdkListener)
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.inventiv.multipaysdk.intent.TOKEN_RECEIVED")
        intentFilter.addAction("com.inventiv.multipaysdk.intent.SDK_CLOSED")
        registerReceiver(sampleReceiver, intentFilter)


        MultiPaySdk.init(
            context = this.applicationContext,
            walletAppToken = info.walletApptoken,
            paymentAppToken = info.paymentAppToken,
            environment = info.environment,
            userId = info.userID,
        )
    }

    override fun onDestroy() {
        unregisterReceiver(sampleReceiver)
        super.onDestroy()
    }

    private val multiPaySdkListener = object : MultiPaySdkListener {
/*
        override fun onTokenReceived(token: String) {
            Log.i(TAG, "${info.environment.name} onTokenReceived: $token")
            getCardInfo(token, this)
        }

        override fun onMultiPaySdkClosed() {
            Log.i(TAG, "${info.environment.name} onMultiPaySdkClosed")
            Toast.makeText(this@MultinetWalletActivity, "MultiPay Sdk Closed", Toast.LENGTH_LONG)
                .show()
        }

        override fun onSingeWalletReceived(singleWallet: SingleWalletResponse) {
            Log.i(TAG, "${info.environment.name} onSingeWalletReceived: $singleWallet")
            walletResponse = singleWallet.wallet
            setWalletUI()
            walletItem.visibility = View.VISIBLE
        }

        override fun onUnSelectWalletReceived(unSelectWallet: UnselectWalletResponse?) {
            Log.d(TAG, "${info.environment.name} onUnSelectWalletReceived: $unSelectWallet")
            walletResponse = null
            walletItem.visibility = View.GONE
        }

        override fun onServiceError(error: String?, code: Int) {
            Log.d(TAG, "${info.environment.name} onServiceError: error = $error code = $code")
            Toast.makeText(
                this@MultinetWalletActivity,
                "onServiceError : $error",
                Toast.LENGTH_LONG
            ).show()
        }

        override fun onConfirmPaymentReceived(sign: String, transferServerRefNo: String) {
            Log.d(
                TAG,
                "${info.environment.name} onConfirmPaymentReceived: $sign  $transferServerRefNo"
            )
            if (sign == generateSignForConfirmPaymentResponse(transferServerRefNo)) {
                Log.d(
                    TAG,
                    "${info.environment.name} onConfirmPaymentReceived: signs matched successfully"
                )
                Toast.makeText(this@MultinetWalletActivity, "Payment succeeded", Toast.LENGTH_LONG)
                    .show()
            } else {
                Log.d(
                    TAG,
                    "${info.environment.name} onConfirmPaymentReceived: signs didn't matched!!"
                )
                Toast.makeText(
                    this@MultinetWalletActivity,
                    "Payment signs didn't match",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        } */
    }
}