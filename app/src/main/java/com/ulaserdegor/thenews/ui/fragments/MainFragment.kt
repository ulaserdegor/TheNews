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

    private lateinit var sourcesAdapter: SourcesAdapter

    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        observeLiveData()
        sourcesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("id", it.id)
                putSerializable("title", it.name)
            }
            findNavController().navigate(
                R.id.action_mainFragment_to_topHeadlinesFragment,
                bundle
            )

        }
        refreshSources()
    }

    private fun observeLiveData() {
        viewModel.apply {
            sourcesLiveData.observe(viewLifecycleOwner, {

                if (sourcesAdapter.itemCount != 0) {
                    Toast.makeText(activity, getString(R.string.updated), Toast.LENGTH_SHORT).show()
                }

                sourcesAdapter.differ.submitList(it)
                rlSources.isRefreshing = false
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

    private fun refreshSources() {
        rlSources.isRefreshing = true
        rlSources.setOnRefreshListener {
            viewModel.getSources()
        }
    }
}
