package com.omtorney.snapcase.presenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omtorney.snapcase.data.model.News
import com.omtorney.snapcase.databinding.ItemNewsBinding

class NewsAdapter(
//    private val onItemClicked: (news: String) -> Unit
) : RecyclerView.Adapter<NewsViewHolder>() {

    private var newsList: List<News> = emptyList()

    fun setNews(newsList: List<News>) {
        this.newsList = newsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = newsList.getOrNull(position)
        with(holder.binding) {
            item?.let {
                newsHeader.text = it.header
                newsPreview.text = it.preview
                newsDate.text = it.date
            }
        }

//        holder.binding.root.setOnClickListener {
//            onItemClicked(newsList[position])
//        }
    }

    override fun getItemCount(): Int = newsList.size
}

class NewsViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root)