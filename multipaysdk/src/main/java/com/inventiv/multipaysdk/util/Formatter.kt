package com.inventiv.multipaysdk.util

internal object Formatter {

    private const val PHONE_NUMBER_REGEX = "(\\d{3})(\\d{3})(\\d{2})(\\d{2})"
    private const val PHONE_NUMBER_REPLACEMENT = "0($1) $2 $3 $4"
    private const val PHONE_NUMBER_MASK_REPLACEMENT = "0($1) ***** $4"

    fun servicePhoneNumber(phoneNumber: String) = stringToNumeric(phoneNumber).substring(1)

    fun stringToNumeric(str: String) = str.replace("[^\\d]".toRegex(), "")

    fun formatPhoneNumber(value: String?, withMask: Boolean): String? {
        return value?.replaceFirst(
            PHONE_NUMBER_REGEX.toRegex(),
            if (withMask) PHONE_NUMBER_MASK_REPLACEMENT else PHONE_NUMBER_REPLACEMENT
        )
    }
}