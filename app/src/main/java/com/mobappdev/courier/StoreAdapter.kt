package com.mobappdev.courier

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobappdev.courier.databinding.ItemMapBinding

class StoreAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val VIEW_TYPE_DEFAULT = 1
    }

    private val differCallback = object : DiffUtil.ItemCallback<Store>() {

        override fun areItemsTheSame(
            oldItem: Store,
            newItem: Store,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Store,
            newItem: Store,
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemMapBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MainViewHolder(binding)
    }

    inner class MainViewHolder(private val binding: ItemMapBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(storeModel: Store) {
            binding.tvTitle.text = storeModel.contactPerson
            binding.cbEnable.isChecked = storeModel.isEnabled ?: false

            binding.cbEnable.setOnCheckedChangeListener { _, isChecked ->
                // Update the isEnabled property of the corresponding Store object
                storeModel.isEnabled = isChecked
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MainViewHolder) {
            val phoneItem = differ.currentList[position]
            holder.bind(phoneItem)
        }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_DEFAULT
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}