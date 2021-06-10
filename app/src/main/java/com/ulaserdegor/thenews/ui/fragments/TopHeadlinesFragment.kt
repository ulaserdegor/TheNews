package com.ulaserdegor.thenews.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ulaserdegor.thenews.R
import com.ulaserdegor.thenews.ui.activities.MainActivity
import com.ulaserdegor.thenews.ui.adapters.TopHeadlinesAdapter
import com.ulaserdegor.thenews.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_top_headlines.*

@AndroidEntryPoint
class TopHeadlinesFragment : Fragment(R.layout.fragment_top_headlines) {

    lateinit var topHeadLinesAdapter: TopHeadlinesAdapter

    private val viewModel: MainViewModel by viewModels()
    private val args: TopHeadlinesFragmentArgs by navArgs()
    private var selectedPage = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        observeLiveData()
        topHeadLinesAdapter.setOnItemClickListener {
            openBrowser(it.url!!)
        }

        (activity as MainActivity).supportActionBar?.title = args.title

        fillList()
        refreshHeadlines()
    }

    private fun observeLiveData() {
        viewModel.apply {
            topHeadlinesLiveData.observe(viewLifecycleOwner, {

                if (topHeadLinesAdapter.itemCount != 0) {
                    Toast.makeText(activity, getString(R.string.updated), Toast.LENGTH_SHORT).show()
                }
                topHeadLinesAdapter.differ.submitList(it)
                rlHeadlines.isRefreshing = false

                if (topHeadLinesAdapter.itemCount == 0) {
                    flipper.showNext()
                }
            })
        }
    }

    private fun setUpRecyclerView() {
        topHeadLinesAdapter = TopHeadlinesAdapter()
        rvHeadlines.apply {
            adapter = topHeadLinesAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        setupListenerPostListScroll()
    }

    private fun setupListenerPostListScroll() {
        val scrollDirectionDown = 1
        var currentListSize = 0

        rvHeadlines.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (!recyclerView.canScrollVertically(scrollDirectionDown)
                        && newState == RecyclerView.SCROLL_STATE_IDLE
                    ) {
                        val listSizeAfterLoading = recyclerView.layoutManager!!.itemCount

                        if (currentListSize != listSizeAfterLoading) {
                            currentListSize = listSizeAfterLoading

                            selectedPage++
                            fillList()
                        }
                    }
                }
            })
    }

    private fun refreshHeadlines() {
        rlHeadlines.setOnRefreshListener {
            fillList()
        }
    }

    private fun fillList() {
        rlHeadlines.isRefreshing = true
        viewModel.getTopHeadlines(args.id, selectedPage)
    }

    private fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}
