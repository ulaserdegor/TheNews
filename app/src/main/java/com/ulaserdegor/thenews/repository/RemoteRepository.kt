package com.ulaserdegor.thenews.repository

import com.ulaserdegor.thenews.data.models.NewsEntity
import com.ulaserdegor.thenews.data.models.SourceModel
import com.ulaserdegor.thenews.data.remote.NewsAPI
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val newsApi: NewsAPI
) : BaseRepository() {

    suspend fun getSources(): MutableList<SourceModel>? {
        return safeApiCall(
            call = { newsApi.getNewsSource() },
            error = "Haber kaynakları listesi çekilirken hata oluştu"
        )?.sources?.toMutableList()
    }


    suspend fun getTopHeadlines(country: String, page: Int): MutableList<NewsEntity>? {
        return safeApiCall(
            call = { newsApi.getTopHeadlines(country, page) },
            error = "Kaynağa ait haber listesi çekilirken hata oluştu"
        )?.articles?.toMutableList()
    }

}