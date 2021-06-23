package com.inventiv.multipaysdk.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.inventiv.multipaysdk.MultiPaySdk
import com.inventiv.multipaysdk.data.model.response.UserPreset

class UserPresetDialog : BottomSheetDialogFragment() {

    private lateinit var buttonStartSdk: MaterialButton
    private lateinit var textInputEditName: TextInputEditText
    private lateinit var textInputEditSurname: TextInputEditText
    private lateinit var textInputEditEmail: TextInputEditText
    private lateinit var textInputEditGsm: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_bottom_user_preset, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonStartSdk = view.findViewById(R.id.button_start_sdk)
        textInputEditName = view.findViewById(R.id.text_input_edit_name)
        textInputEditSurname = view.findViewById(R.id.text_input_edit_surname)
        textInputEditEmail = view.findViewById(R.id.text_input_edit_email)
        textInputEditGsm = view.findViewById(R.id.text_input_edit_gsm)
    }

    override fun getTheme(): Int = R.style.Theme_DialogStyle

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        buttonStartSdk.setOnClickListener {
            val userPreset = UserPreset(
                textInputEditName.text.toString(),
                textInputEditSurname.text.toString(),
                textInputEditEmail.text.toString(),
                textInputEditGsm.text.toString()
            )
            MultiPaySdk.start(requireActivity(), null, userPreset)
            dismiss()
        }
    }
}