package com.example.firstapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapp.data.ContentModel
import com.example.firstapp.databinding.ContentListItemBinding


//val itemClick: (ContentModel) -> Unit
//method: get(parameter) ContentModel, return void

class ContentListAdapter(private val onClickListener: OnClickListener ) :
    ListAdapter<ContentModel,
            ContentListAdapter.ContentViewHolder>(DiffCallback){

    class ContentViewHolder(private var binding: ContentListItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(content: ContentModel) {
            binding.item = content

            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ContentModel>() {
        override fun areItemsTheSame(oldItem: ContentModel, newItem: ContentModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ContentModel, newItem: ContentModel): Boolean {
            return oldItem.title == newItem.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        return ContentViewHolder(ContentListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        val content = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(content)
        }
        holder.bind(content)
    }

    class OnClickListener(val clickListener: (content:ContentModel) -> Unit) {
        fun onClick(content:ContentModel) = clickListener(content)
    }
}