package com.ulaserdegor.thenews.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ulaserdegor.thenews.R
import com.ulaserdegor.thenews.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopHeadlinesFragment : Fragment(R.layout.fragment_top_headlines) {

    private val viewModel: MainViewModel by viewModels()
    private val args: TopHeadlinesFragmentArgs by navArgs()


}