package com.example.slideshowapp.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.slideshowapp.BR
import com.example.slideshowapp.R

/**
 * アニメーション一覧用のAdapter
 */
class AnimationAdapter : ListAdapter<String, AnimationAdapter.BindingHolder>(ITEM_COMPARATOR) {
    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }
    }

    //region MARK: - public fields
    var onSelectedItem: (position: Int) -> Unit = { _ -> }
    //endregion

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingHolder {
        val layoutId = R.layout.item_animation_list
        // DataBinding
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(parent.context),
                layoutId,
                parent,
                false
            )
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        val data = getItem(position)

        holder.binding.setVariable(BR.data, data)
        holder.binding.executePendingBindings()
        holder.itemView.setOnClickListener {
            onSelectedItem(position)
        }
    }

    class BindingHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
}