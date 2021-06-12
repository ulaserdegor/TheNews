package com.ulaserdegor.thenews.repository

import com.ulaserdegor.thenews.data.local.SavedNewsDao
import com.ulaserdegor.thenews.data.models.NewsEntity
import javax.inject.Inject

class LocaleRepository @Inject constructor(
    private val db: SavedNewsDao
) : BaseRepository() {

    suspend fun insertNewsToDb(news: NewsEntity) = db.insertNews(news)

    fun deleteNewsWithTitle(news: NewsEntity) = db.deleteNewsWithTitle(news.title!!)

    fun getSavedNews() = db.getSavedNews()
}