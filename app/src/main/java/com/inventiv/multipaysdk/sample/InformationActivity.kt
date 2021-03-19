package com.inventiv.multipaysdk.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.inventiv.multipaysdk.Environment

class InformationActivity : AppCompatActivity() {

    companion object {

        const val EXTRA_ENVIRONMENT = "extra_environment"

        fun newIntent(context: Context, environment: com.inventiv.multipaysdk.Environment) =
            Intent(context, InformationActivity::class.java).apply {
                putExtra(EXTRA_ENVIRONMENT, environment)
            }
    }

    private lateinit var environment: Environment

    private lateinit var editWalletAppToken: AppCompatEditText
    private lateinit var editPaymentAppToken: AppCompatEditText
    private lateinit var editSaltKey: AppCompatEditText
    private lateinit var editUserID: AppCompatEditText

    private lateinit var buttonSave: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        environment = intent.getSerializableExtra(EXTRA_ENVIRONMENT) as Environment

        editWalletAppToken = findViewById(R.id.edit_wallet_app_token)
        editPaymentAppToken = findViewById(R.id.edit_payment_app_token)
        editSaltKey = findViewById(R.id.edit_salt_key)
        editUserID = findViewById(R.id.edit_user_id)
        buttonSave = findViewById(R.id.button_save)

        buttonSave.setOnClickListener {
            val walletApptoken = editWalletAppToken.text.toString()
            val paymentAppToken = editPaymentAppToken.text.toString()
            val saltKey = editSaltKey.text.toString()
            val userID = editUserID.text.toString()

            val infos = Infos(
                environment,
                walletApptoken,
                paymentAppToken,
                saltKey,
                userID
            )

            val strInfos = Gson().toJson(infos)
            getSharedPref().edit().putString(PREF_INFOS, strInfos).apply()

            startActivity(MultinetWalletActivity.newIntent(this@InformationActivity, infos))
            finish()
        }

    }
}