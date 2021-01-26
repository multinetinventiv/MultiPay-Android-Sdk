package com.inventiv.multipaysdk.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

internal abstract class BaseFragment<V : ViewBinding> : Fragment() {

    private var binding: V? = null

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createBinding(inflater, container, savedInstanceState).also { binding = it }.root
    }

    protected abstract fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): V

    protected fun requireBinding(): V = requireNotNull(binding)

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}