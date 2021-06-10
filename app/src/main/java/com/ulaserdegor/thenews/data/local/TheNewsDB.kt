package com.ulaserdegor.thenews.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ulaserdegor.thenews.data.models.NewsEntity

@Database(
    entities = [NewsEntity::class],
    version = 1
)
abstract class TheNewsDB : RoomDatabase() {

    abstract fun getSavedNewsDao(): SavedNewsDao
}