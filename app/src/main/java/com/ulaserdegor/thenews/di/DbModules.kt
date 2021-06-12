package com.ulaserdegor.thenews.di

import android.content.Context
import androidx.room.Room
import com.ulaserdegor.thenews.data.local.TheNewsDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideTheNewsDB(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        TheNewsDB::class.java,
        "the_news_db.db"
    ).allowMainThreadQueries().build()

    @Singleton
    @Provides
    fun provideSavedNewsDao(db: TheNewsDB) = db.getSavedNewsDao()
}