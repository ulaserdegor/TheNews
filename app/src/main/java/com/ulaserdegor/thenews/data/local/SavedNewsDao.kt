package com.ulaserdegor.thenews.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ulaserdegor.thenews.data.models.NewsEntity

@Dao
interface SavedNewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: NewsEntity): Long

    @Query("SELECT * FROM saved_news")
    fun getSavedNews(): LiveData<List<NewsEntity>>

    @Delete
    suspend fun deleteNews(news: NewsEntity)
}