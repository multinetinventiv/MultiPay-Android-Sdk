package com.inventiv.multipaysdk.sample

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.DialogFragment

interface ConfirmPaymentDialogListener {
    fun onConfirmPayment(paymentInfos: PaymentInfos)
}

class ConfirmPaymentDialogFragment : DialogFragment() {

    private lateinit var editRequestId: AppCompatEditText
    private lateinit var editTransferReferenceNumber: AppCompatEditText
    private lateinit var editMerchantReferenceNumber: AppCompatEditText
    private lateinit var editTerminalReferenceNumber: AppCompatEditText
    private lateinit var editAmount: AppCompatEditText
    private lateinit var editProductId: AppCompatEditText
    private lateinit var editReferenceNumber: AppCompatEditText
    private lateinit var editSign: AppCompatEditText
    private lateinit var buttonConfirmPayment: Button
    private var confirmPaymentDialogListener: ConfirmPaymentDialogListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        confirmPaymentDialogListener = context as ConfirmPaymentDialogListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editRequestId = view.findViewById(R.id.edit_request_id)
        editTransferReferenceNumber = view.findViewById(R.id.edit_transfer_reference_number)
        editMerchantReferenceNumber = view.findViewById(R.id.edit_merchant_reference_number)
        editTerminalReferenceNumber = view.findViewById(R.id.edit_terminal_reference_number)
        editAmount = view.findViewById(R.id.edit_amount)
        editProductId = view.findViewById(R.id.edit_product_id)
        editReferenceNumber = view.findViewById(R.id.edit_reference_number)
        editSign = view.findViewById(R.id.edit_sign)
        buttonConfirmPayment = view.findViewById(R.id.button_confirm_payment)

        buttonConfirmPayment.setOnClickListener {
            confirmPayment()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun confirmPayment() {
        val requestId = editRequestId.text.toString()
        val transferReferenceNumber = editTransferReferenceNumber.text.toString()
        val merchantReferenceNumber = editMerchantReferenceNumber.text.toString()
        val terminalReferenceNumber = editTerminalReferenceNumber.text.toString()
        val amount = editAmount.text.toString()
        val productId = editProductId.text.toString()
        val referenceNumber = editReferenceNumber.text.toString()
        val sign = editSign.text.toString()

        val paymentInfos = PaymentInfos(
            requestId = requestId,
            transferReferenceNumber = transferReferenceNumber,
            merchantReferenceNumber = merchantReferenceNumber,
            terminalReferenceNumber = terminalReferenceNumber,
            amount = amount,
            productId = productId,
            referenceNumber = referenceNumber,
            sign = sign
        )
        dismiss()
        confirmPaymentDialogListener?.onConfirmPayment(paymentInfos)
    }

    override fun onDestroy() {
        super.onDestroy()
        confirmPaymentDialogListener = null
    }
}