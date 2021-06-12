package com.ulaserdegor.thenews.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ulaserdegor.thenews.data.models.NewsEntity

@Dao
interface SavedNewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: NewsEntity): Long

    @Query("SELECT * FROM saved_news")
    fun getSavedNews(): LiveData<List<NewsEntity>>

    @Query("DELETE FROM saved_news WHERE title = :title")
    fun deleteNewsWithTitle(title: String)

}