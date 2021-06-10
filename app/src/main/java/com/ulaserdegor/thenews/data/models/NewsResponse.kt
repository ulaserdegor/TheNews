package com.ulaserdegor.thenews.data.models

data class NewsResponse(
    val news: MutableList<NewsEntity>,
    val status: String,
    val totalResults: Int
)