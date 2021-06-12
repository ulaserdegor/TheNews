package com.ulaserdegor.thenews.data.local

import androidx.room.TypeConverter
import com.ulaserdegor.thenews.data.models.SourceModel

class Converters {

    @TypeConverter
    fun fromSource(source: SourceModel) = source.name

    @TypeConverter
    fun toSource(name: String) = SourceModel(name, name, "")
}
