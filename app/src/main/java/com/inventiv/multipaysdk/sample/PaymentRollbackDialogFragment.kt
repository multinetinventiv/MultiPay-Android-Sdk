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

interface PaymentRollbackDialogListener {
    fun onPaymentRollback(paymentRollback: PaymentRollback)
}

class PaymentRollbackDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(paymentInfos: PaymentInfos): PaymentRollbackDialogFragment {
            return PaymentRollbackDialogFragment().apply {
                val args = Bundle().apply {
                    putParcelable(ARG_PAYMENT_INFOS, paymentInfos)
                }
                arguments = args
            }
        }
    }

    private lateinit var paymentInfos: PaymentInfos

    private lateinit var editRequestId: AppCompatEditText
    private lateinit var editRollbackReferenceNumber: AppCompatEditText
    private lateinit var editReferenceNumberType: AppCompatEditText
    private lateinit var editSign: AppCompatEditText
    private lateinit var buttonPaymentRollback: Button
    private var paymentReversalDialogListener: PaymentRollbackDialogListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        paymentReversalDialogListener = context as PaymentRollbackDialogListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentInfos = arguments?.getParcelable<PaymentInfos>(ARG_PAYMENT_INFOS)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment_rollback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editRequestId = view.findViewById(R.id.edit_request_id)
        editRollbackReferenceNumber = view.findViewById(R.id.edit_rollback_reference_number)
        editReferenceNumberType = view.findViewById(R.id.edit_reference_number_type)
        editSign = view.findViewById(R.id.edit_sign)
        buttonPaymentRollback = view.findViewById(R.id.button_payment_rollback)

        buttonPaymentRollback.setOnClickListener {
            paymentRollback()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun paymentRollback() {
        val requestId = editRequestId.text.toString()
        val rollbackReferenceNumber = editRollbackReferenceNumber.text.toString()
        val referenceNumberType = editReferenceNumberType.text.toString().toInt()
        val sign = editSign.text.toString()

        val paymentRollback = PaymentRollback(
            requestId = requestId,
            terminalReferenceNumber = paymentInfos.terminalReferenceNumber,
            merchantReferenceNumber = paymentInfos.merchantReferenceNumber,
            rollbackReferenceNumber = rollbackReferenceNumber,
            reason = paymentInfos.reason!!,
            referenceNumberType = referenceNumberType,
            referenceNumber = if (referenceNumberType == 0) paymentInfos.transferReferenceNumber else paymentInfos.transferServerRefNo!!,
            sign = sign
        )
        dismiss()
        paymentReversalDialogListener?.onPaymentRollback(paymentRollback)
    }

    override fun onDestroy() {
        super.onDestroy()
        paymentReversalDialogListener = null
    }
}