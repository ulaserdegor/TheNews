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
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localeRepository: LocaleRepository
) : ViewModel() {

    val sourcesLiveData = MutableLiveData<MutableList<SourceModel>>()
    private val topHeadlinesLiveDataHolder = MutableLiveData<MutableList<NewsEntity>>()
    val topHeadlinesLiveData = MutableLiveData<MutableList<NewsEntity>>()

    init {
        getSources()
    }

    fun getSources() = viewModelScope.launch {
        val sources = remoteRepository.getSources()
        sourcesLiveData.postValue(sources)
    }

    fun getTopHeadlines(pageSize: String, page: Int? = 1) = viewModelScope.launch {
        val topHeadlines = remoteRepository.getTopHeadlines(pageSize, page!!)
        topHeadlinesLiveData.postValue(topHeadlines)
        topHeadlinesLiveDataHolder.postValue(topHeadlines)
    }


    fun searchHeadlines(filterText: String) = viewModelScope.launch {

        if (filterText.isEmpty()) {
            topHeadlinesLiveData.value = topHeadlinesLiveDataHolder.value
            return@launch
        }

        topHeadlinesLiveData.value = topHeadlinesLiveDataHolder.value?.filter { l ->
            l.title!!.toLowerCase(Locale.getDefault()).contains(
                filterText
            )
        }!!.toMutableList()

    }


    fun saveNews(newsEntity: NewsEntity) = viewModelScope.launch {
        localeRepository.insertNewsToDb(newsEntity)
    }

    fun deleteNews(newsEntity: NewsEntity) = viewModelScope.launch {
        localeRepository.deleteNews(newsEntity)
    }

    fun getSavedNews() = localeRepository.getSavedNews()
}