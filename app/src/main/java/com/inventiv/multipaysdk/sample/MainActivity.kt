package com.inventiv.multipaysdk.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.inventiv.multipaysdk.Environment

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
            goToInfo(Environment.DEV)
        }
        buttonPilot.setOnClickListener {
            goToInfo(Environment.PILOT)
        }
        buttonTest.setOnClickListener {
            goToInfo(Environment.TEST)
        }
        buttonProduction.setOnClickListener {
            goToInfo(Environment.PRODUCTION)
        }

        val sharedPref = getSharedPref()
        val strInfos = sharedPref.getString(PREF_INFOS, String())
        if (!strInfos.isNullOrEmpty()) {
            val infos = Gson().fromJson(strInfos, Infos::class.java)
            startActivity(MultinetWalletActivity.newIntent(this@MainActivity, infos))
            finish()
        }
    }

    private fun goToInfo(environment: Environment) {
        startActivity(InformationActivity.newIntent(this@MainActivity, environment))
    }
}