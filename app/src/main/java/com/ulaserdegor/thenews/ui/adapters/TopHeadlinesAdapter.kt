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

    private var glideOptions: RequestOptions = RequestOptions()
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
    private var savedNews = listOf<NewsEntity>()

    companion object {
        var onItemClickListener: NewsItemClickListener? = null
    }

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

                //db de ki filtrelemeyi title a göre yaptım. çünkü apiden gelen response da unique bi id yok.
                if (!savedNews.isNullOrEmpty()) {
                    it.isFavorited = savedNews.map { x -> x.title }.contains(it.title)
                } else {
                    it.isFavorited = false
                }
                if (it.isFavorited!!) {
                    btnHeadlineFavorite.text = context.getString(R.string.remove_favorite)
                } else {
                    btnHeadlineFavorite.text = context.getString(R.string.add_favorite)
                }

                setOnClickListener {
                    onItemClickListener?.itemClicked(headline)
                }

                btnHeadlineFavorite.setOnClickListener { _ ->

                    onItemClickListener?.itemFavoriteClicked(headline, it.isFavorited!!)
                    /* differ.currentList.get(headline!).isFavorited!! = !headline.isFavorited!!
                     notifyDataSetChanged()*/
                }
            }
        }
    }

    fun updateSavedList(list: List<NewsEntity>) {
        savedNews = list
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: NewsItemClickListener) {
        onItemClickListener = listener
    }

    interface NewsItemClickListener {

        fun itemClicked(newsEntity: NewsEntity)
        fun itemFavoriteClicked(newsEntity: NewsEntity, isFavorited: Boolean)
    }

    class HeadlinesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}