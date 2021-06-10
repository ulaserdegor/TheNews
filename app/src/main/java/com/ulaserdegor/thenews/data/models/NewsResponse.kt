package com.ulaserdegor.thenews.data.models

data class NewsResponse(
    val articles: MutableList<NewsEntity>,
    val status: String,
    val totalResults: Int
)