package com.inventiv.multipaysdk.sample

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.inventiv.multipaysdk.MultiPaySdkListener
import com.inventiv.multipaysdk.data.model.response.SingleWalletResponse
import com.inventiv.multipaysdk.data.model.response.UnselectWalletResponse

class MultinetWalletActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context) = Intent(context, MultinetWalletActivity::class.java)
    }

    private lateinit var sampleReceiver: SampleReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multinet_wallet)

        sampleReceiver = SampleReceiver(multiPaySdkListener)
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.inventiv.multipaysdk.intent.TOKEN_RECEIVED")
        intentFilter.addAction("com.inventiv.multipaysdk.intent.SDK_CLOSED")
        registerReceiver(sampleReceiver, intentFilter)
    }

    private val multiPaySdkListener = object : MultiPaySdkListener {
    }
}