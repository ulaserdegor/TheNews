package com.ulaserdegor.thenews.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ulaserdegor.thenews.R
import com.ulaserdegor.thenews.data.models.NewsEntity
import com.ulaserdegor.thenews.ui.activities.MainActivity
import com.ulaserdegor.thenews.ui.adapters.TopHeadlinesAdapter
import com.ulaserdegor.thenews.ui.viewmodels.MainViewModel
import com.ulaserdegor.thenews.utils.Constants.Companion.PULL_NEWS_DELAY
import com.ulaserdegor.thenews.utils.dateIsSame
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_top_headlines.*
import kotlinx.coroutines.*
import java.util.*

@AndroidEntryPoint
class TopHeadlinesFragment : Fragment(R.layout.fragment_top_headlines),
    TopHeadlinesAdapter.NewsItemClickListener {

    lateinit var topHeadLinesAdapter: TopHeadlinesAdapter

    private val viewModel: MainViewModel by viewModels()
    private val args: TopHeadlinesFragmentArgs by navArgs()
    private var selectedPage = 1

    lateinit var mainHandler: Handler

    private val updateListTask = object : Runnable {
        override fun run() {

            //arama yapılırken refresh atılması önlendi
            if (etSearch.text.isNullOrEmpty()) {

                getSavedNews()
                viewModel.getTopHeadlines(args.id, selectedPage)
            }
            mainHandler.postDelayed(this, PULL_NEWS_DELAY)
        }
    }

    private var latestUpdateTime = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        observeLiveData()
        topHeadLinesAdapter.setOnItemClickListener(this)
        (activity as MainActivity).supportActionBar?.title = args.title

        refreshHeadlines()
        initSearchHeadlines()

        getSavedNews()
        //fillList()

        rlHeadlines.isRefreshing = true
        mainHandler = Handler(Looper.getMainLooper())

    }

    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(updateListTask)
    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(updateListTask)
    }

    private fun observeLiveData() {
        viewModel.apply {
            topHeadlinesLiveData.observe(viewLifecycleOwner, {

                topHeadLinesAdapter.differ.submitList(it)
                rlHeadlines.isRefreshing = false

                if (latestUpdateTime.isNotEmpty()) {

                    //apiden yeniden eskiye tarih sıralı gelen cevaplardan ilkini en son tutulan tarih ile karşılaştırdım.
                    // ğer aynı değil ise farklı bir değer gelmiş demektir ve uyarı gösterilir
                    if (!dateIsSame(
                            latestUpdateTime,
                            topHeadLinesAdapter.differ.currentList.first().publishedAt.toString()
                        )
                    ) {
                        Toast.makeText(activity, getString(R.string.updated), Toast.LENGTH_SHORT)
                            .show()
                    }

                }

                latestUpdateTime =
                    topHeadLinesAdapter.differ.currentList.first().publishedAt.toString()
/*



                if (topHeadLinesAdapter.differ.currentList != it && etSearch.text.isNullOrEmpty()) {
                    Toast.makeText(activity, getString(R.string.updated), Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Log.i(
                        "Güncel Haber Yok", "Yeni haber verisi olmadığı için güncellendi" +
                                " yazmadı. Test ederken genelde apiden 20 nin altında veri olduğu için bu log'u yazmak zorunda kaldım." +
                                "Constants dosyasına gidip PULL_NEWS_COUNT değerini azaltarak bunu deneyebilirsiniz."
                    )
                }*/


                if (topHeadLinesAdapter.itemCount == 0) {
                    tvEmptyView.visibility = View.VISIBLE
                } else {
                    tvEmptyView.visibility = View.GONE
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

    private fun initSearchHeadlines() {
        etSearch.addTextChangedListener { editable ->

            GlobalScope.launch(Dispatchers.Main) {
                delay(1000)
                editable?.let {
                    //boş olma kontorlü viewmodel de yapıldı
                    viewModel.searchHeadlines(editable.toString().toLowerCase(Locale.getDefault()))
                }
            }
        }
    }

    override fun itemClicked(newsEntity: NewsEntity) {
        openBrowser(newsEntity.url!!)
    }

    override fun itemFavoriteClicked(newsEntity: NewsEntity) {
        if (newsEntity.isFavorited!!) {
            viewModel.deleteNews(newsEntity)
        } else {
            viewModel.saveNews(newsEntity)
        }

        topHeadLinesAdapter.notifyDataSetChanged()
    }

    private fun getSavedNews() {

        viewModel.getSavedNews().observe(viewLifecycleOwner, {

            topHeadLinesAdapter.savedNews = it
            topHeadLinesAdapter.notifyDataSetChanged()
        })
    }

}
