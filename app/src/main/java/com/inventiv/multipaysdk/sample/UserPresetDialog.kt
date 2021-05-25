package com.inventiv.multipaysdk.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.inventiv.multipaysdk.MultiPaySdk
import com.inventiv.multipaysdk.data.model.response.UserPreset
import kotlinx.android.synthetic.main.dialog_bottom_user_preset.*

class UserPresetDialog : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_bottom_user_preset, container, false)
    }

    override fun getTheme(): Int = R.style.Theme_DialogStyle

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        button_start_sdk.setOnClickListener {
            val userPreset = UserPreset(
                text_input_edit_name.text.toString(),
                text_input_edit_surname.text.toString(),
                text_input_edit_email.text.toString(),
                text_input_edit_gsm.text.toString()
            )
            MultiPaySdk.start(requireActivity(), null, userPreset)
        }
    }
}