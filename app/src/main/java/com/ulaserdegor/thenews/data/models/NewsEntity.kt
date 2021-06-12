package com.ulaserdegor.thenews.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "saved_news")
data class NewsEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: SourceModel?,
    val title: String?,
    val url: String?,
    val urlToImage: String?,
    var isFavorited: Boolean? = false
) : Serializable