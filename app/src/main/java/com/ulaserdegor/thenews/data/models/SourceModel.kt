package com.ulaserdegor.thenews.data.models

import java.io.Serializable

data class SourceModel(
    val id: String?,
    val name: String?,
    val description: String?,
    val country: String?
) : Serializable