package com.inventiv.multipaysdk.ui.wallet

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.inventiv.multipaysdk.R
import com.inventiv.multipaysdk.data.model.response.WalletResponse
import com.inventiv.multipaysdk.databinding.ItemWalletSingleSelectMultipaySdkBinding
import com.inventiv.multipaysdk.util.themeColor

internal class WalletAdapter(private val clickListener: WalletListener) :
    ListAdapter<WalletListItem, WalletAdapter.ViewHolder>(WalletDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val walletItem = getItem(position)
        holder.bind(walletItem, clickListener)
    }

    class ViewHolder private constructor(private val binding: ItemWalletSingleSelectMultipaySdkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(walletListItem: WalletListItem, clickListener: WalletListener) {
            binding.textWalletNameMultipaySdk.text = walletListItem.walletResponse.name
            binding.textWalletBalanceMultipaySdk.text = walletListItem.walletResponse.balance
            binding.textWalletNumberMultipaySdk.text = walletListItem.walletResponse.maskedNumber
            binding.radiobtnWalletMultipaySdk.isChecked = walletListItem.isChecked
            if (walletListItem.isChecked) {
                binding.layoutWalletItemMultipaySdk.setBackgroundColor(
                    binding.layoutWalletItemMultipaySdk.context.themeColor(R.attr.colorControlHighlight)
                )
            } else {
                binding.layoutWalletItemMultipaySdk.setBackgroundColor(Color.TRANSPARENT)
            }
            binding.imageWalletMultipaySdk.setImageUrl(walletListItem.walletResponse.imageUrl)
            if (walletListItem.walletResponse.isSelected != null && walletListItem.walletResponse.isSelected) {
                binding.root.isClickable = false
                binding.root.alpha = 0.3f
            } else {
                binding.root.isClickable = true
                binding.root.alpha = 1f
                binding.root.setOnClickListener {
                    clickListener.onClick(walletListItem.walletResponse)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemWalletSingleSelectMultipaySdkBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

internal class WalletListener(val clickListener: (walletResponse: WalletResponse) -> Unit) {
    fun onClick(walletResponse: WalletResponse) = clickListener(walletResponse)
}

internal class WalletDiffCallback : DiffUtil.ItemCallback<WalletListItem>() {
    override fun areItemsTheSame(oldItem: WalletListItem, newItem: WalletListItem): Boolean {
        return oldItem.walletResponse.token == newItem.walletResponse.token
    }

    override fun areContentsTheSame(oldItem: WalletListItem, newItem: WalletListItem): Boolean {
        return oldItem == newItem
    }
}

internal data class WalletListItem(
    val walletResponse: WalletResponse,
    var isChecked: Boolean = false
)