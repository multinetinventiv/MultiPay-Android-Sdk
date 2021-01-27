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
    private lateinit var editUserID: AppCompatEditText
    private lateinit var editMerchantReferenceNumber: AppCompatEditText
    private lateinit var editTerminalReferenceNumber: AppCompatEditText
    private lateinit var editAmount: AppCompatEditText
    private lateinit var editProductId: AppCompatEditText
    private lateinit var editSign: AppCompatEditText

    private lateinit var buttonSave: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        environment = intent.getSerializableExtra(EXTRA_ENVIRONMENT) as Environment

        editWalletAppToken = findViewById(R.id.edit_wallet_app_token)
        editPaymentAppToken = findViewById(R.id.edit_payment_app_token)
        editUserID = findViewById(R.id.edit_user_id)
        editMerchantReferenceNumber = findViewById(R.id.edit_merchant_reference_number)
        editTerminalReferenceNumber = findViewById(R.id.edit_terminal_reference_number)
        editAmount = findViewById(R.id.edit_amount)
        editProductId = findViewById(R.id.edit_product_id)
        editSign = findViewById(R.id.edit_sign)
        buttonSave = findViewById(R.id.button_save)

        buttonSave.setOnClickListener {
            val walletApptoken = editWalletAppToken.text.toString()
            val paymentAppToken = editPaymentAppToken.text.toString()
            val userID = editUserID.text.toString()
            val merchantReferenceNumber = editMerchantReferenceNumber.text.toString()
            val terminalReferenceNumber = editTerminalReferenceNumber.text.toString()
            val amount = editAmount.text.toString()
            val productId = editProductId.text.toString()
            val sign = editSign.text.toString()

            val infos = Infos(
                environment,
                walletApptoken,
                paymentAppToken,
                userID,
                merchantReferenceNumber,
                terminalReferenceNumber,
                amount,
                productId,
                sign
            )

            val strInfos = Gson().toJson(infos)
            getSharedPref().edit().putString(PREF_INFOS, strInfos).apply()

            startActivity(MultinetWalletActivity.newIntent(this@InformationActivity, infos))

        }

    }
}