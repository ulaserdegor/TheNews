package com.ulaserdegor.thenews.data.remote

import com.ulaserdegor.thenews.data.models.NewsResponse
import com.ulaserdegor.thenews.data.models.SourcesResponse
import com.ulaserdegor.thenews.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/sources")
    suspend fun getNewsSource(
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<SourcesResponse>

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country")
        countryCode: String,
        @Query("page")
        page: Int,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>

}