package com.ulaserdegor.thenews.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    val newsLiveData = MutableLiveData<MutableList<SourceModel>>()

    init {
        getSources()
    }

    fun getSources() = viewModelScope.launch {
        val sources = remoteRepository.getSources()
        newsLiveData.postValue(sources)
    }

}