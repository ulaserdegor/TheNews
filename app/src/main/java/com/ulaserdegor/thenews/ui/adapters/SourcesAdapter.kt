package com.ulaserdegor.thenews.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ulaserdegor.thenews.R
import com.ulaserdegor.thenews.data.models.SourceModel
import kotlinx.android.synthetic.main.item_row_source.view.*

class SourcesAdapter : RecyclerView.Adapter<SourcesAdapter.SourcesViewHolder>() {

    private val differCallBack = object : DiffUtil.ItemCallback<SourceModel>() {

        override fun areItemsTheSame(oldItem: SourceModel, newItem: SourceModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: SourceModel, newItem: SourceModel) =
            oldItem == newItem

    }

    val differ = AsyncListDiffer(this, differCallBack)
    private var onItemClickListener: ((SourceModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SourcesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_row_source,
                parent,
                false
            )
        )

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: SourcesViewHolder, position: Int) {
        val source = differ.currentList[position]
        holder.itemView.apply {
            source?.let {
                tvSourceTitle.text = it.name
                tvSourceDescription.text = it.description

                setOnClickListener {
                    onItemClickListener?.let {
                        it(source)
                    }
                }
            }
        }
    }

    fun setOnItemClickListener(listener: (SourceModel) -> Unit) {
        onItemClickListener = listener
    }

    class SourcesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}