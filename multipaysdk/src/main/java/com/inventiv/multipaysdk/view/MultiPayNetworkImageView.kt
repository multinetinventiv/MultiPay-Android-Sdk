package com.inventiv.multipaysdk.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import com.android.volley.toolbox.NetworkImageView
import com.inventiv.multipaysdk.MultiPaySdk
import com.inventiv.multipaysdk.R

internal class MultiPayNetworkImageView(context: Context, attrs: AttributeSet) :
    NetworkImageView(context, attrs) {

    companion object {
        const val NO_RESOURCE_VALUE = 0
        val DEFAULT_IMAGE_RES = NO_RESOURCE_VALUE
        val DEFAULT_ERROR_RES = NO_RESOURCE_VALUE
    }

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val attributes: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.MultiPayNetworkImageView)

        val errorImageRes: Int =
            attributes.getResourceId(
                R.styleable.MultiPayNetworkImageView_error_image_res_id,
                DEFAULT_ERROR_RES
            )

        val defaultImageRes: Int =
            attributes.getResourceId(
                R.styleable.MultiPayNetworkImageView_default_image_res_id,
                DEFAULT_IMAGE_RES
            )

        val imageRes: Int =
            attributes.getResourceId(
                R.styleable.MultiPayNetworkImageView_image_res_id,
                NO_RESOURCE_VALUE
            )

        val imgSrc: String? =
            attributes.getString(R.styleable.MultiPayNetworkImageView_img_url)

        if (errorImageRes > 0) {
            setErrorImageResId(errorImageRes)
        }

        if (defaultImageRes > 0) {
            setDefaultImageResId(defaultImageRes)
        }

        if (!imgSrc.isNullOrEmpty()) {
            setImageUrl(imgSrc)
        } else if (imageRes > 0) {
            setImageResource(imageRes)
        }

        attributes.recycle()
    }

    fun setImageUrl(url: String?) {
        setImageUrl(url, MultiPaySdk.getComponent().volleyManager().getVolleyImageLoader())
    }

    fun setErrorImage(@DrawableRes resId: Int) {
        setErrorImageResId(resId)
    }

    fun setDefaultImage(@DrawableRes resId: Int) {
        setDefaultImageResId(resId)
    }
}