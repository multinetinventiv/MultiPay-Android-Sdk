package com.inventiv.multipaysdk.util

import android.util.Patterns
import com.inventiv.multipaysdk.MultiPaySdk
import com.inventiv.multipaysdk.R
import com.inventiv.multipaysdk.data.model.type.ValidationErrorType

internal object Validator {

    const val INPUT_TYPE_EMAIL = 0
    const val INPUT_TYPE_GSM = 1
    const val INPUT_TYPE_UNDEFINED = 2
    const val NAME_INPUT_MIN_LENGTH = 2
    private const val PASSWORD_MIN = 8
    private const val PASSWORD_MAX = 20
    private const val MASKED_LENGTH_PHONE = 16
    private const val MASKED_PREFIX_PHONE = "0"

    fun getInputType(str: String): Int {
        return when {
            str.isEmpty() -> {
                INPUT_TYPE_UNDEFINED
            }
            atLeastOneAlpha(str) -> {
                INPUT_TYPE_EMAIL
            }
            else -> {
                INPUT_TYPE_GSM
            }
        }
    }

    private fun atLeastOneAlpha(str: String): Boolean {
        return str.matches(".*[a-zA-Z]+.*".toRegex())
    }


    fun validEmail(email: String): Boolean {
        return (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches())
    }

    fun validPassword(password: String): Boolean {
        return (password.isNotEmpty()
                && password.length >= PASSWORD_MIN && password.length <= PASSWORD_MAX)
    }

    fun validGsmWithMask(gsm: String): Boolean {
        return (gsm.isNotEmpty()
                && gsm.length == MASKED_LENGTH_PHONE && gsm.startsWith(MASKED_PREFIX_PHONE))
    }

    fun getValidationError(type: ValidationErrorType): String {
        when {
            type === ValidationErrorType.NAME -> {
                return MultiPaySdk.getComponent().getString(R.string.validation_name_multipay_sdk)
            }
            type === ValidationErrorType.SURNAME -> {
                return MultiPaySdk.getComponent()
                    .getString(R.string.validation_surname_multipay_sdk)
            }
            type === ValidationErrorType.EMAIL -> {
                return MultiPaySdk.getComponent().getString(R.string.validation_email_multipay_sdk)
            }
            type === ValidationErrorType.GSM -> {
                return MultiPaySdk.getComponent().getString(R.string.validation_gsm_multipay_sdk)
            }
            type === ValidationErrorType.EMAIL_GSM -> {
                return MultiPaySdk.getComponent()
                    .getString(R.string.validation_email_or_gsm_multipay_sdk)
            }
            else -> return StringUtils.EMPTY
        }
    }
}