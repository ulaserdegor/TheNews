package com.ulaserdegor.thenews.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ulaserdegor.thenews.data.remote.NewsAPI
import com.ulaserdegor.thenews.utils.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Singleton
    @Provides
    fun provideNewsApi(retrofit: Retrofit.Builder): NewsAPI {
        return retrofit
            .build()
            .create(NewsAPI::class.java)
    }

}
