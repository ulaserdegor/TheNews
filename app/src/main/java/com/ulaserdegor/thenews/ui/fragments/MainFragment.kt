package com.ulaserdegor.thenews.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ulaserdegor.thenews.R
import com.ulaserdegor.thenews.ui.adapters.SourcesAdapter
import com.ulaserdegor.thenews.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_fragment.*


@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {

    lateinit var sourcesAdapter: SourcesAdapter

    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        observeLiveData()
        sourcesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("country", it.country)
            }
            findNavController().navigate(
                R.id.action_mainFragment_to_topHeadlinesFragment,
                bundle
            )

        }
        refreshNews()
    }

    private fun observeLiveData() {
        viewModel.apply {
            newsLiveData.observe(viewLifecycleOwner, {
                sourcesAdapter.differ.submitList(it)
            })
        }
    }

    private fun setUpRecyclerView() {
        sourcesAdapter = SourcesAdapter()
        rvSources.apply {
            adapter = sourcesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun refreshNews() {
        rlSources.setOnRefreshListener {
            viewModel.getSources()
            Toast.makeText(activity, getString(R.string.updated), Toast.LENGTH_SHORT).show()
            rlSources.isRefreshing = false
        }
    }
}
