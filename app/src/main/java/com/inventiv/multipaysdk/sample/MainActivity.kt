package com.inventiv.multipaysdk.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.inventiv.multipaysdk.Environment
import com.inventiv.multipaysdk.MultiPaySdk
import com.inventiv.multipaysdk.data.model.type.Language

class MainActivity : AppCompatActivity() {

    private lateinit var buttonDev: MaterialButton
    private lateinit var buttonPilot: MaterialButton
    private lateinit var buttonTest: MaterialButton
    private lateinit var buttonProduction: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonDev = findViewById(R.id.btn_dev_env)
        buttonPilot = findViewById(R.id.btn_pilot_env)
        buttonTest = findViewById(R.id.btn_test_env)
        buttonProduction = findViewById(R.id.btn_production_env)

        buttonDev.setOnClickListener {
            MultiPaySdk.init(
                context = this.applicationContext,
                walletAppToken = "",
                paymentAppToken = "",
                environment = Environment.DEV,
                language = Language.TR
            )
            goToWallet()
        }
        buttonPilot.setOnClickListener {
            MultiPaySdk.init(
                context = this.applicationContext,
                walletAppToken = "",
                paymentAppToken = "",
                environment = Environment.PILOT,
                language = Language.TR
            )
            goToWallet()
        }
        buttonTest.setOnClickListener {
            MultiPaySdk.init(
                context = this.applicationContext,
                walletAppToken = "",
                paymentAppToken = "",
                environment = Environment.TEST,
                language = Language.TR
            )
            goToWallet()
        }
        buttonProduction.setOnClickListener {
            MultiPaySdk.init(
                context = this.applicationContext,
                walletAppToken = "",
                paymentAppToken = "",
                environment = Environment.PRODUCTION,
                language = Language.TR
            )
            goToWallet()
        }
    }

    private fun goToWallet() {
        startActivity(MultinetWalletActivity.newIntent(this@MainActivity))
    }
}