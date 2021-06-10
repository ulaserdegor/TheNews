package com.ulaserdegor.thenews.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulaserdegor.thenews.data.models.NewsEntity
import com.ulaserdegor.thenews.data.models.SourceModel
import com.ulaserdegor.thenews.repository.LocaleRepository
import com.ulaserdegor.thenews.repository.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localeRepository: LocaleRepository
) : ViewModel() {

    val sourcesLiveData = MutableLiveData<MutableList<SourceModel>>()
    val topHeadlinesLiveData = MutableLiveData<MutableList<NewsEntity>>()

    init {
        getSources()
    }

    fun getSources() = viewModelScope.launch {
        val sources = remoteRepository.getSources()
        sourcesLiveData.postValue(sources)
    }

    fun getTopHeadlines(country: String, page: Int? = 1) = viewModelScope.launch {
        val topHeadlines = remoteRepository.getTopHeadlines(country, page!!)
        topHeadlinesLiveData.postValue(topHeadlines)
    }

}