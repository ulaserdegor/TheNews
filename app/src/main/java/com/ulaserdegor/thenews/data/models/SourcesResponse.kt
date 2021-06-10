package com.ulaserdegor.thenews.data.models

data class SourcesResponse(
    val sources: MutableList<SourceModel>,
    val status: String
)