package com.ulaserdegor.thenews.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.ulaserdegor.thenews.R
import com.ulaserdegor.thenews.data.models.NewsEntity
import kotlinx.android.synthetic.main.item_row_news.view.*


class TopHeadlinesAdapter : RecyclerView.Adapter<TopHeadlinesAdapter.HeadlinesViewHolder>() {

    var glideOptions: RequestOptions = RequestOptions()
        .centerCrop()
        .placeholder(R.drawable.progress_animation)
        .error(R.drawable.null_img)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .priority(Priority.HIGH)
        .dontAnimate()
        .dontTransform()

    private val differCallBack = object : DiffUtil.ItemCallback<NewsEntity>() {

        override fun areItemsTheSame(oldItem: NewsEntity, newItem: NewsEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: NewsEntity, newItem: NewsEntity) =
            oldItem == newItem

    }

    val differ = AsyncListDiffer(this, differCallBack)
    private var onItemClickListener: ((NewsEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HeadlinesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_row_news,
                parent,
                false
            )
        )

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: HeadlinesViewHolder, position: Int) {
        val headline = differ.currentList[position]
        holder.itemView.apply {
            headline?.let {
                Glide.with(this)
                    .load(it.urlToImage)
                    .apply(glideOptions)
                    .into(ivHeadlineImage)
                tvHeadlineTitle.text = it.title
                tvHeadlineDescription.text = it.description

                // TODO: 10.06.2021 buton eventleri db kısmında yapılacak
                if (3 == 3) {
                    btnHeadlineFavorite.text = "Okuma Listeme Ekle"
                } else {
                    btnHeadlineFavorite.text = "Okuma Listemden Çıkar"
                }

                setOnClickListener {
                    onItemClickListener?.let {
                        it(headline)
                    }
                }
            }
        }
    }

    fun setOnItemClickListener(listener: (NewsEntity) -> Unit) {
        onItemClickListener = listener
    }

    class HeadlinesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}